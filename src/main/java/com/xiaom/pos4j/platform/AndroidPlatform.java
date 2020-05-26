package com.xiaom.pos4j.platform;


import com.xiaom.pos4j.platform.image.AndroidImageProcess;
import com.xiaom.pos4j.platform.image.ImageProcess;
import com.xiaom.pos4j.platform.log.AndroidLoggerImpl;
import com.xiaom.pos4j.platform.log.Logger;

/**
 * @author hous
 */
public class AndroidPlatform extends Platform {


    public static Platform buildIfSupported() {
        try {
            Class.forName("android.graphics.Bitmap");
            return new AndroidPlatform();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public Logger getLogger() {
        return new AndroidLoggerImpl();
    }

    @Override
    public ImageProcess getImageProcess() {
        return new AndroidImageProcess();
    }
}
