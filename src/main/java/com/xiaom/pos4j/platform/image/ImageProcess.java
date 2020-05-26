package com.xiaom.pos4j.platform.image;


import com.xiaom.pos4j.element.Image;

import java.io.InputStream;

/**
 * @author hous
 */
public interface ImageProcess {


    Image imagePixelsFromFilePath(String pathName, int width, int height);

    Image imagePixelsFromInputStream(InputStream is, int width, int height);

}
