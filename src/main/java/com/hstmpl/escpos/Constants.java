package com.hstmpl.escpos;

import java.util.regex.Pattern;

/**
 * @author hous
 */
public interface Constants {

    int PRINTER_PORT = 9100;

    String LOCAL_HOST = "127.0.0.1";

    String CHARSET_GBK = "GBK";

    String CHARSET_UTF8 = "UTF-8";

    int SOCKET_TIMEOUT = 10000;

    int BUFFER_SIZE = 512;

    Pattern PARSE_PATTERN = Pattern.compile("#\\{\\s*(.+?)\\s*\\}");

    String TEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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

    String XSD_CONTENT = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "           targetNamespace=\"print_template\"\n" +
            "           xmlns=\"print_template\">\n" +
            "\n" +
            "    <xs:simpleType name=\"subexpression\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:pattern value=\"#\\{.+\\}\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "    <xs:simpleType name=\"size\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:enumeration value=\"w1h1\"/>\n" +
            "            <xs:enumeration value=\"w1h2\"/>\n" +
            "            <xs:enumeration value=\"w1h3\"/>\n" +
            "            <xs:enumeration value=\"w2h1\"/>\n" +
            "            <xs:enumeration value=\"w3h1\"/>\n" +
            "            <xs:enumeration value=\"w2h3\"/>\n" +
            "            <xs:enumeration value=\"w3h2\"/>\n" +
            "            <xs:enumeration value=\"w2h2\"/>\n" +
            "            <xs:enumeration value=\"w3h3\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "    <xs:simpleType name=\"align\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:enumeration value=\"left\"/>\n" +
            "            <xs:enumeration value=\"center\"/>\n" +
            "            <xs:enumeration value=\"right\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "    <xs:simpleType name=\"overflow\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:enumeration value=\"hidden\"/>\n" +
            "            <xs:enumeration value=\"newline\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "    <xs:simpleType name=\"imageType\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:enumeration value=\"image\"/>\n" +
            "            <xs:enumeration value=\"qrcode\"/>\n" +
            "            <xs:enumeration value=\"barcode\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "    <xs:simpleType name=\"repeat\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:enumeration value=\"none\"/>\n" +
            "            <xs:enumeration value=\"fill\"/>\n" +
            "            <xs:enumeration value=\"auto\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "\n" +
            "    <xs:element name=\"text\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:attribute name=\"value\" type=\"xs:string\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"bold\" type=\"xs:boolean\" default=\"false\"/>\n" +
            "            <xs:attribute name=\"size\" type=\"size\"/>\n" +
            "            <xs:attribute name=\"align\" type=\"align\"/>\n" +
            "            <xs:attribute name=\"underline\" type=\"xs:boolean\"/>\n" +
            "            <xs:attribute name=\"repeat\" type=\"repeat\"/>\n" +
            "            <xs:attribute name=\"margin\" type=\"xs:string\"/>\n" +
            "            <xs:attribute name=\"marginLeft\" type=\"xs:integer\"/>\n" +
            "            <xs:attribute name=\"marginTop\" type=\"xs:integer\"/>\n" +
            "            <xs:attribute name=\"marginRight\" type=\"xs:integer\"/>\n" +
            "            <xs:attribute name=\"marginBottom\" type=\"xs:integer\"/>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "    <xs:element name=\"section\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:choice maxOccurs=\"unbounded\">\n" +
            "                <xs:element ref=\"text\"/>\n" +
            "            </xs:choice>\n" +
            "            <xs:attribute name=\"marginTop\" type=\"xs:integer\"/>\n" +
            "            <xs:attribute name=\"marginBottom\" type=\"xs:integer\"/>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "\n" +
            "    <xs:element name=\"image\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:attribute name=\"type\" type=\"imageType\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"value\" type=\"xs:string\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"width\" type=\"xs:integer\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"height\" type=\"xs:integer\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"align\" type=\"align\"/>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "\n" +
            "    <xs:element name=\"td\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:attribute name=\"value\" type=\"xs:string\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"weight\" type=\"xs:int\" use=\"required\"/>\n" +
            "            <xs:attribute name=\"align\" type=\"align\"/>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "    <xs:element name=\"tr\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:choice maxOccurs=\"unbounded\">\n" +
            "                <xs:element ref=\"td\"/>\n" +
            "            </xs:choice>\n" +
            "            <xs:attribute name=\"list\" type=\"xs:string\"/>\n" +
            "            <xs:attribute name=\"item\" type=\"xs:string\"/>\n" +
            "            <xs:attribute name=\"bold\" type=\"xs:boolean\"/>\n" +
            "            <xs:attribute name=\"size\" type=\"size\"/>\n" +
            "            <xs:attribute name=\"overflow\" type=\"overflow\"/>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "    <xs:element name=\"table\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:choice maxOccurs=\"unbounded\">\n" +
            "                <xs:element ref=\"tr\"/>\n" +
            "            </xs:choice>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "    <xs:element name=\"block\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:choice maxOccurs=\"unbounded\">\n" +
            "                <xs:element ref=\"text\"/>\n" +
            "                <xs:element ref=\"table\"/>\n" +
            "                <xs:element ref=\"block\"/>\n" +
            "                <xs:element ref=\"image\"/>\n" +
            "                <xs:element ref=\"section\"/>\n" +
            "            </xs:choice>\n" +
            "            <xs:attribute name=\"test\" type=\"xs:string\"/>\n" +
            "            <xs:attribute name=\"list\" type=\"xs:string\"/>\n" +
            "            <xs:attribute name=\"item\" type=\"xs:string\"/>\n" +
            "        </xs:complexType>\n" +
            "    </xs:element>\n" +
            "\n" +
            "    <xs:element name=\"document\">\n" +
            "        <xs:complexType>\n" +
            "            <xs:choice maxOccurs=\"unbounded\" minOccurs=\"0\">\n" +
            "                <xs:element ref=\"text\"/>\n" +
            "                <xs:element ref=\"section\"/>\n" +
            "                <xs:element ref=\"image\"/>\n" +
            "                <xs:element ref=\"table\"/>\n" +
            "                <xs:element ref=\"block\"/>\n" +
            "            </xs:choice>\n" +
            "        </xs:complexType>\n" +
            "\n" +
            "    </xs:element>\n" +
            "\n" +
            "</xs:schema>";
}