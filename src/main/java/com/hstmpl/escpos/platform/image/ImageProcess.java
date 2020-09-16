package com.hstmpl.escpos.platform.image;


import com.hstmpl.escpos.element.Image;

import java.io.InputStream;

/**
 * @author hous
 */
public interface ImageProcess {


    Image imagePixelsFromFilePath(String pathName, int width, int height);

    Image imagePixelsFromInputStream(InputStream is, int width, int height);

}
