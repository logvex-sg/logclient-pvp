# logclient-pvp

Fabric Minecraft client mod (anarchy/PvP utility client) for Minecraft 1.21.11.
Base package: `com.logvex.logclient`.

## Build

Gradle is not installed system-wide; it lives at `/workspace/tools/gradle-9.8.0/bin/gradle`.
Loom requires **JDK 21** for the Java toolchain while Gradle itself runs on the installed JDK.

```bash
sudo apt-get install -y openjdk-21-jdk-headless      # toolchain target (required)
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64   # JDK running Gradle itself
PATH=$JAVA_HOME/bin:$PATH /workspace/tools/gradle-9.8.0/bin/gradle build --no-daemon
```

Artifacts: `build/libs/logclient-1.0.0.jar` and `logclient-1.0.0-sources.jar`.

## Verifying mixins at runtime

A compile pass runs the Mixin annotation processor and will fail the build on bad
injection targets, but only a real launch proves the refmap remaps. Run the dev
client headlessly:

```bash
sudo apt-get install -y xvfb
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
PATH=$JAVA_HOME/bin:$PATH timeout 600 xvfb-run -a /workspace/tools/gradle-9.8.0/bin/gradle runClient --no-daemon
```

`runClient` never exits on its own; a `timeout` kill (exit 124) is expected. Success
looks like `LogClient v1.0.0 loaded 40 modules` with no `InvalidInjection` /
`Mixin apply` errors. OpenAL/ALSA, flite/narrator and Realms auth failures are
expected noise in this sandbox and are unrelated to the mod. Each launch rewrites
`run/` and creates a crash-report directory; both are gitignored.

## Inspecting Minecraft internals

The merged, remapped Minecraft jar is cached by Loom. Use it to check real method
signatures and bytecode before writing an `@At` target:

```bash
MCJAR=$(find .gradle/loom-cache/minecraftMaven -name 'minecraft-merged-*.jar' | head -1)
/usr/lib/jvm/java-25-openjdk-amd64/bin/javap -p -c -cp "$MCJAR" net.minecraft.client.MinecraftClient
```

Prefer `javap -p -c` over guessing names: several 1.21.x signatures are non-obvious
(e.g. `Keyboard.onKey(long, int, KeyInput)`, `Mouse.onMouseButton(long, MouseInput, int)`,
`ChatScreen.sendMessage(String, boolean)`).

## Architecture

- `module/` — `Module` base + `ModuleManager`. Categories: `combat`, `visual`,
  `player`, `misc`, `optimization`, `hud`.
  - `onTick()` runs only while a world is loaded.
  - `onClientTick()` runs every tick, including menus/disconnect screens
    (needed by `AutoReconnect`).
  - `isToggleable() == false` modules act on keybind without changing state.
- `setting/` — typed settings serialized by `config/ConfigManager` to
  `config/logclient.json`.
- `gui/` — `ClickGuiScreen` (open with Right Shift) + widgets.
- `mixin/` — all hooks, registered in `src/main/resources/logclient.mixins.json`.
- `LogClient` exposes static helpers (`mc()`, `noFog()`) that mixins call; each
  returns `null` when the module is absent or disabled so hooks stay one-line.

## Mixin gotchas hit in this codebase

- Do not inject `@At("AFTER")` a `GETFIELD`. The field value is still on the stack
  and a no-arg callback there produces an `ArrayIndexOutOfBoundsException` at apply
  time. `FastPlace` therefore targets the `PUTFIELD` of `itemUseCooldown` inside
  `doItemUse` instead of the per-tick decrement.
- `FogModifier.applyStartEndModifier` is abstract; injecting into it does not work.
  `NoFog` instead mixes each concrete subclass (`WaterFogModifier`, `LavaFogModifier`,
  `PowderSnowFogModifier`, `BlindnessEffectFogModifier`, `DarknessEffectFogModifier`).
- Inner mixin classes are listed in the JSON as `Outer$Inner`.
- `EntityRenderState.displayName` is dereferenced by the nametag renderer; set it to
  `Text.empty()` rather than `null` when hiding names.
