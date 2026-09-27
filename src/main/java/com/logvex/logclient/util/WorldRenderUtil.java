package com.logvex.logclient.util;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.visual.EspModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Projects world positions into screen space and draws them with the GUI renderer.
 * Minecraft 1.21.11 removed the public line render layers, so overlays are drawn on top of
 * the finished frame instead of inside the world render pass.
 */
public final class WorldRenderUtil {
    private WorldRenderUtil() {
    }

    public static Matrix4f viewMatrix(Camera camera) {
        Quaternionf rotation = new Quaternionf(camera.getRotation()).conjugate();
        return new Matrix4f().rotation(rotation);
    }

    public static Matrix4f projectionMatrix(double fov, int width, int height) {
        return new Matrix4f().perspective((float) Math.toRadians(fov), width / (float) height, 0.05f, 1000f);
    }

    /** Returns null when the point sits behind the camera. */
    public static Vector4f project(MinecraftClient mc, Matrix4f view, Matrix4f projection, Vec3d world, float tickDelta) {
        Camera camera = mc.gameRenderer.getCamera();
        if (camera == null) {
            return null;
        }
        Vec3d cameraPos = camera.getCameraPos();
        Vector4f relative = new Vector4f(
                (float) (world.x - cameraPos.x),
                (float) (world.y - cameraPos.y),
                (float) (world.z - cameraPos.z),
                1f);
        view.transform(relative);
        if (relative.z >= 0) {
            return null;
        }
        Vector4f clip = new Vector4f(relative.x, relative.y, relative.z, 1f);
        projection.transform(clip);
        if (clip.w <= 0) {
            return null;
        }
        clip.div(clip.w);
        return clip;
    }

    public static boolean onScreen(Vector4f clip) {
        return clip != null && clip.x >= -1.1f && clip.x <= 1.1f && clip.y >= -1.1f && clip.y <= 1.1f;
    }

    public static int screenX(Vector4f clip, int width) {
        return Math.round((clip.x * 0.5f + 0.5f) * width);
    }

    public static int screenY(Vector4f clip, int height) {
        return Math.round((0.5f - clip.y * 0.5f) * height);
    }

    public static void drawBox(DrawContext context, Matrix4f view, Matrix4f projection, Box box, int color,
                               float tickDelta, MinecraftClient mc) {
        double[][] corners = {
                {box.minX, box.minY, box.minZ}, {box.maxX, box.minY, box.minZ},
                {box.maxX, box.maxY, box.minZ}, {box.minX, box.maxY, box.minZ},
                {box.minX, box.minY, box.maxZ}, {box.maxX, box.minY, box.maxZ},
                {box.maxX, box.maxY, box.maxZ}, {box.minX, box.maxY, box.maxZ}};
        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}};
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();
        for (int[] edge : edges) {
            double[] a = corners[edge[0]];
            double[] b = corners[edge[1]];
            Vector4f pa = project(mc, view, projection, new Vec3d(a[0], a[1], a[2]), tickDelta);
            Vector4f pb = project(mc, view, projection, new Vec3d(b[0], b[1], b[2]), tickDelta);
            if (pa == null || pb == null) {
                continue;
            }
            RenderUtil.line(context, screenX(pa, width), screenY(pa, height),
                    screenX(pb, width), screenY(pb, height), color);
        }
    }

    public static void drawEntityLabel(DrawContext context, Matrix4f view, Matrix4f projection, Entity entity,
                                       String label, int color, int background, float tickDelta, MinecraftClient mc) {
        Vec3d top = entity.getLerpedPos(tickDelta).add(0, entity.getHeight() + 0.4, 0);
        Vector4f clip = project(mc, view, projection, top, tickDelta);
        if (!onScreen(clip)) {
            return;
        }
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();
        int x = screenX(clip, width);
        int y = screenY(clip, height);
        int textWidth = RenderUtil.textWidth(label);
        context.fill(x - textWidth / 2 - 2, y - 1, x + textWidth / 2 + 2, y + RenderUtil.fontHeight() + 1, background);
        RenderUtil.centeredText(context, label, x, y, color);
    }

    public static EspModule esp() {
        return LogClient.getInstance().getModuleManager().getModule(EspModule.class);
    }
}
