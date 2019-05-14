package com.cin.pos.platform;


import com.cin.pos.platform.image.ImageProcess;
import com.cin.pos.platform.log.DefaultLoggerImpl;
import com.cin.pos.platform.log.Logger;

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

}
