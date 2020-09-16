package com.hstmpl.escpos.platform;


import com.hstmpl.escpos.platform.image.AndroidImageProcess;
import com.hstmpl.escpos.platform.image.ImageProcess;
import com.hstmpl.escpos.platform.log.AndroidLoggerImpl;
import com.hstmpl.escpos.platform.log.Logger;

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
