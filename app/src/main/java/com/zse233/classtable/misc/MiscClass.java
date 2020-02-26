package com.zse233.classtable.misc;

public class MiscClass {
    private static boolean isScheduled = true;
    private static String oneWord = "";
    private static String bingPic = "";
    private static String userKey = "";
    private static int termCode = 34;

    public static int getTermCode() {
        return termCode;
    }

    public static void setTermCode(int termCode) {
        MiscClass.termCode = termCode;
    }

    public static String getUserKey() {
        return userKey;
    }

    public static void setUserKey(String userKey) {
        MiscClass.userKey = userKey;
    }

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
