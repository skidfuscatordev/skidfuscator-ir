package dev.skidfuscator.util;

public class MiscHelper {
    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        return decodeJvmVersion(version);
    }

    public static int decodeJvmVersion(String version) {
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);
    }

    public static boolean isJmod() {
        return MiscHelper.getJavaVersion() > 8;
    }
}
