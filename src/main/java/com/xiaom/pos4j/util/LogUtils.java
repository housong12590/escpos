package com.xiaom.pos4j.util;

import com.xiaom.pos4j.platform.Platform;
import com.xiaom.pos4j.platform.log.Logger;

/**
 * @author hous
 */
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
