package com.cin.pos.platform.log;

public interface Logger {
    
    void debug(String message);

    void info(String message);

    void error(String message);

    void warn(String message);
}
