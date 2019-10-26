package com.ciin.pos.common;


public class Platform {

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

    public static boolean is32Bit() {
        return !is64Bit();
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
