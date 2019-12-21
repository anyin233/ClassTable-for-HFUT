package com.zse233.classtable.misc;

public class MiscClass {
    private static boolean isScheduled = true;

    public static boolean isAtScheduled() {
        return isScheduled;
    }

    public static void atScheduled(boolean isScheduled) {
        MiscClass.isScheduled = isScheduled;
    }
}
