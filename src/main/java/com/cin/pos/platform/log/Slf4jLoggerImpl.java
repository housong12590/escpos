package com.cin.pos.platform.log;


import org.slf4j.LoggerFactory;

public class Slf4jLoggerImpl implements Logger {

    private org.slf4j.Logger logger = LoggerFactory.getLogger("escpos");

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

}
