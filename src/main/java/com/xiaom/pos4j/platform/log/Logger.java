package com.xiaom.pos4j.platform.log;

/**
 * @author hous
 */
public interface Logger {
    
    void debug(String message);

    void info(String message);

    void error(String message);

    void warn(String message);
}
