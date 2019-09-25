package com.ciin.pos.platform;


import com.ciin.pos.platform.image.ImageProcess;
import com.ciin.pos.platform.log.DefaultLoggerImpl;
import com.ciin.pos.platform.log.Logger;

public class Platform {

    private static final Platform PLATFORM = findPlatform();

    public static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        Platform java = JavaPlatform.buildIfSupported();
        if (java != null) {
            return java;
        }
        Platform android = AndroidPlatform.buildIfSupported();
        if (android != null) {
            return android;
        }
        return new Platform();
    }

    public Logger getLogger() {
        return new DefaultLoggerImpl();
    }

    public ImageProcess getImageProcess() {
        return null;
    }

    private static int osType;

    static {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux")) {
            if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
                osType = 4;
            } else {
                osType = 1;
            }
        } else if (osName.startsWith("Windows")) {
            osType = 2;
        } else if (osName.startsWith("Mac")) {
            osType = 3;
        }
    }

    public static boolean is64Bit() {
        String model = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if (model != null) {
            return "64".equals(model);
        }
        String arch = System.getProperty("os.arch");
        return "amd64".equals(arch)
                || "x86-64".equals(arch)
                || "ia64".equals(arch)
                || "ppc64".equals(arch)
                || "ppc64le".equals(arch)
                || "sparcv9".equals(arch)
                || "mips64".equals(arch)
                || "mips64el".equals(arch);
    }

    public static boolean isWindows() {
        return osType == 2;
    }

    public static boolean isAndroid() {
        return osType == 4;
    }

    public static boolean isLinux() {
        return osType == 1;
    }

    public static boolean isMac() {
        return osType == 3;
    }

}
