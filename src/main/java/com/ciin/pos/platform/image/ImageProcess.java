package com.ciin.pos.platform.image;


import com.ciin.pos.element.Image;

import java.io.InputStream;

public interface ImageProcess {


    Image imagePixelsFromFilePath(String pathName, int width, int height);

    Image imagePixelsFromInputStream(InputStream is, int width, int height);

}
