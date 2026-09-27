package com.logvex.logclient.util;

public final class Stopwatch {
    private long lastMs;

    public Stopwatch() {
        reset();
    }

    public void reset() {
        lastMs = System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - lastMs;
    }

    public boolean hasElapsed(long ms) {
        return elapsed() >= ms;
    }

    public boolean hasElapsed(long ms, boolean reset) {
        if (elapsed() >= ms) {
            if (reset) {
                reset();
            }
            return true;
        }
        return false;
    }
}
