package com.cin.pos.platform;


import com.cin.pos.platform.image.ImageProcess;
import com.cin.pos.platform.image.JavaImageProcess;
import com.cin.pos.platform.log.DefaultLoggerImpl;
import com.cin.pos.platform.log.Logger;

public class JavaPlatform extends Platform {

    public static Platform buildIfSupported() {
        try {
            Class.forName("java.awt.image.BufferedImage");
            return new JavaPlatform();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public Logger getLogger() {
        return new DefaultLoggerImpl();
//        try {
//            Class.forName("org.slf4j.Logger");
//            return new Slf4jLoggerImpl();
//        } catch (ClassNotFoundException ignored) {
//
//        }
//        return new DefaultLoggerImpl();
    }

    @Override
    public ImageProcess getImageProcess() {
        return new JavaImageProcess();
    }
}
