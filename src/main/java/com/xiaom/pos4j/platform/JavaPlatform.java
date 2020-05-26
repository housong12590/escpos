package com.xiaom.pos4j.platform;


import com.xiaom.pos4j.platform.image.ImageProcess;
import com.xiaom.pos4j.platform.image.JavaImageProcess;
import com.xiaom.pos4j.platform.log.DefaultLoggerImpl;
import com.xiaom.pos4j.platform.log.Logger;

/**
 * @author hous
 */
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
