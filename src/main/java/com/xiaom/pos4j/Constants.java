package com.xiaom.pos4j;

import java.util.regex.Pattern;

/**
 * @author hous
 */
public final class Constants {

    public static final int PRINTER_PORT = 9100;

    public static final String LOCAL_HOST = "127.0.0.1";

    public static final String CHARSET_GBK = "GBK";

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final int SOCKET_TIMEOUT = 10000;

    public static final int BUFFER_SIZE = 512;

    public static Pattern REPLACE_PATTERN = Pattern.compile("\\$\\{\\s*(.*?)\\s*\\}");

    public static Pattern PARSE_PATTERN = Pattern.compile("#\\{\\s*(.*?)\\s*\\}");

    public static String TEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<document xmlns=\"print_template\">\n" +
            "    <text value=\"测试打印\" align=\"center\" bold=\"true\" size=\"big\" marginBottom=\"1\"/>\n" +
            "    <text value=\"  POS打印机，打印方式为行式热敏，打印速度为220mm/s(Max)，纸宽79.5±0.5mm。\" marginTop=\"1\"/>\n" +
            "    <text marginTop=\"1\"\n" +
            "          value=\"  热敏打印机打印速度快、噪声低、可靠性好、打印质量高、无需色带，免除了日常维护的烦恼。操作简单，应用领域广泛，尤其适用于商业收款机，银行POS及各类收条打印。\"/>\n" +
            "    <text\n" +
            "        value=\"  POS printer is line thermal sensitive with a printing speed of 220mm/s(Max) and a paper width of 79.5±0.5mm.\"\n" +
            "        marginTop=\"1\" marginRight=\"3\" marginLeft=\"3\"/>\n" +
            "\n" +
            "    <text\n" +
            "        value=\"T80 thermal printer has the advantages of fast printing speed, low noise, good reliability, high printing quality and no need of ribbon, eliminating the trouble of daily maintenance.It is easy to operate and widely used, especially for commercial cash register, bank POS and all kinds of receipt printing.\"\n" +
            "        marginTop=\"1\" marginLeft=\"3\" marginRight=\"3\"/>\n" +
            "\n" +
            "    <text value=\"-\" repeat=\"fill\" marginTop=\"1\"/>\n" +
            "\n" +
            "    <image type=\"qrcode\" value=\"https://github.com/housong12590/escpos\" width=\"180\" height=\"180\"\n" +
            "           align=\"center\"/>\n" +
            "\n" +
            "    <text value=\"打印时间: #{date}\" align=\"right\" marginTop=\"1\"/>\n" +
            "\n" +
            "</document>";
}