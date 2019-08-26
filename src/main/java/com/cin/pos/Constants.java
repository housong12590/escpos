package com.cin.pos;

import java.util.regex.Pattern;

public final class Constants {

    public static final int PRINTER_PORT = 9100;

    public static final String LOCAL_HOST = "127.0.0.1";

    public static final String CHARSET_GBK = "GBK";

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final int SOCKET_TIMEOUT = 3000;

    public static Pattern REPLACE_PATTERN = Pattern.compile("\\$\\{\\s*(.*?)\\s*}");

    public static Pattern PARSE_PATTERN = Pattern.compile("#\\{\\s*(.*?)\\s*}");
}