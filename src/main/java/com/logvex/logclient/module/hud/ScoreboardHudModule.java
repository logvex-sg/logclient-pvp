package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardHudModule extends HudModule {
    public ScoreboardHudModule() {
        super("Scoreboard", "Renders the sidebar scoreboard", 4, 176);
    }

    private List<String> lines(MinecraftClient mc) {
        List<String> lines = new ArrayList<>();
        if (mc.world == null) {
            return lines;
        }
        Scoreboard scoreboard = mc.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (objective == null) {
            return lines;
        }
        lines.add(objective.getDisplayName().getString());
        List<ScoreboardEntry> entries = new ArrayList<>(scoreboard.getScoreboardEntries(objective));
        entries.sort(Comparator.comparingInt(ScoreboardEntry::value).reversed());
        for (ScoreboardEntry entry : entries) {
            if (entry.hidden()) {
                continue;
            }
            Team team = scoreboard.getScoreHolderTeam(entry.owner());
            String prefix = team == null || team.getColor() == null ? "" : team.getColor().toString();
            lines.add(prefix + entry.name().getString() + " " + entry.value());
        }
        return lines;
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        int width = 0;
        for (String line : lines(mc)) {
            width = Math.max(width, RenderUtil.textWidth(line));
        }
        return width + 6;
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return lines(mc).size() * (RenderUtil.fontHeight() + 2) + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        List<String> lines = lines(mc);
        if (lines.isEmpty()) {
            return;
        }
        int width = getWidth(mc);
        int screenWidth = mc.getWindow().getScaledWidth();
        int x = screenWidth - width - getX();
        int y = getY();
        drawBackground(context, x, y, width, getHeight(mc));
        int lineY = y + 2;
        for (int i = 0; i < lines.size(); i++) {
            RenderUtil.text(context, lines.get(i), x + 3, lineY, i == 0 ? Theme.ACCENT : Theme.TEXT);
            lineY += RenderUtil.fontHeight() + 2;
        }
    }
}
