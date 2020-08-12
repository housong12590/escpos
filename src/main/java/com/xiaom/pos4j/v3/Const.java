package com.xiaom.pos4j.v3;

import java.util.regex.Pattern;

public interface Const {

     Pattern PARSE_PATTERN = Pattern.compile("#\\{\\s*(.+?)\\s*\\}");
}
