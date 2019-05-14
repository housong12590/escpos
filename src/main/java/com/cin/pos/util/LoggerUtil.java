package com.cin.pos.util;

import com.cin.pos.platform.Platform;
import com.cin.pos.platform.log.Logger;

public class LoggerUtil {

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
