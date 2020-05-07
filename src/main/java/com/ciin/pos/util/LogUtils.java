package com.ciin.pos.util;

import com.ciin.pos.platform.Platform;
import com.ciin.pos.platform.log.Logger;

public class LogUtils {

    private static Logger logger = Platform.get().getLogger();

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }
}
