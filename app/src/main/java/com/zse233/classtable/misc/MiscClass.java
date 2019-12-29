package com.zse233.classtable.misc;

public class MiscClass {
    private static boolean isScheduled = true;
    private static String oneWord = "";
    private static String bingPic = "";

    public static String getBingPic() {
        return bingPic;
    }

    public static void setBingPic(String bingPic) {
        MiscClass.bingPic = bingPic;
    }

    public static String getOneWord() {
        return oneWord;
    }

    public static void setOneWord(String oneWord) {
        MiscClass.oneWord = oneWord;
    }

    public static boolean isAtScheduled() {
        return isScheduled;
    }

    public static void atScheduled(boolean isScheduled) {
        MiscClass.isScheduled = isScheduled;
    }
}
