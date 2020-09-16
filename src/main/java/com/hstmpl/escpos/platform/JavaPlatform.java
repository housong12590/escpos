package com.hstmpl.escpos.platform;


import com.hstmpl.escpos.platform.image.ImageProcess;
import com.hstmpl.escpos.platform.image.JavaImageProcess;
import com.hstmpl.escpos.platform.log.DefaultLoggerImpl;
import com.hstmpl.escpos.platform.log.Logger;

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
