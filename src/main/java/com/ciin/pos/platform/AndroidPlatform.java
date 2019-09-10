package com.ciin.pos.platform;

//import android.graphics.Bitmap;


import com.ciin.pos.platform.image.AndroidImageProcess;
import com.ciin.pos.platform.image.ImageProcess;
import com.ciin.pos.platform.log.AndroidLoggerImpl;
import com.ciin.pos.platform.log.Logger;

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
