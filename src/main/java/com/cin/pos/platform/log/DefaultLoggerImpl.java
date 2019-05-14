package com.cin.pos.platform.log;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultLoggerImpl implements Logger {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    private static final String TAG = "escpos";

    public DefaultLoggerImpl() {

    }

    @Override
    public void debug(String message) {
        print("debug", message);
    }

    @Override
    public void info(String message) {
        print("info", message);
    }

    @Override
    public void error(String message) {
        print("error", message);
    }

    @Override
    public void warn(String message) {
        print("warn", message);
    }

    private void print(String level, String message) {
        String date = this.format.format(new Date());
        String name = Thread.currentThread().getName();
        String text = String.format("%s [%s] %s %s", date, level.toUpperCase(), name, message);
        System.out.println(text);
    }
}
