package com.cin.pos;

import com.cin.pos.convert.ConverterKit;
import com.cin.pos.device.Device;
import com.cin.pos.device.Paper_80;
import com.cin.pos.element.Document;
import com.cin.pos.element.Element;
import com.cin.pos.parser.PrintTemplate;
import com.cin.pos.util.JSONUtil;

import java.util.Map;

public class Index {

    public static void main(String[] args) {
        String tmpStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<document xmlns=\"http://www.w3school.com.cn\"\n" +
                "          xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "          xsi:schemaLocation=\"http://www.w3school.com.cn print_template.xsd\">\n" +
                "\n" +
                "    <text value=\"${keys.mch}\" align=\"center\" bold=\"true\" size=\"big\"/>\n" +
                "    <text value=\"${keys.title}\" align=\"center\" bold=\"true\" size=\"big\"/>\n" +
                "    <text value=\"单号: ${keys.sn}\" marginTop=\"1\"/>\n" +
                "    <section>\n" +
                "        <text value=\"台号: ${keys.table_name}\"/>\n" +
                "        <text value=\" \" repeat=\"auto\"/>\n" +
                "        <text value=\"人数: ${keys.guests}位\"/>\n" +
                "    </section>\n" +
                "    <section>\n" +
                "        <text value=\"开台: ${keys.ctime}\"/>\n" +
                "        <text value=\" \" repeat=\"auto\"/>\n" +
                "        <text value=\"员工: ${keys.oper_name}\"/>\n" +
                "    </section>\n" +
                "    <text value=\"-\" repeat=\"fill\"/>\n" +
                "    <table>\n" +
                "        <tr bold=\"true\">\n" +
                "            <td value=\" 品项名称\" weight=\"9\"/>\n" +
                "            <td value=\" 单价\" weight=\"2\" align=\"right\"/>\n" +
                "            <td value=\" 数量\" weight=\"1\" align=\"right\"/>\n" +
                "            <td value=\" 金额\" weight=\"1\" align=\"right\"/>\n" +
                "        </tr>\n" +
                "        <tr repeat=\"true\" repeatKey=\"#{goods}\">\n" +
                "            <td value=\" #{name}\" weight=\"9\"/>\n" +
                "            <td value=\" #{unit}\" weight=\"2\" align=\"right\"/>\n" +
                "            <td value=\" #{num}\" weight=\"1\" align=\"right\"/>\n" +
                "            <td value=\" #{amount}\" weight=\"1\" align=\"right\"/>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "    <text value=\"-\" repeat=\"fill\"/>\n" +
                "    <text value=\"${keys.sum_value}\" align=\"right\" bold=\"true\"/>\n" +
                "    <text value=\"未结金额: ${keys.wjje}\" condition=\"#{keys.wjje}\" align=\"right\" bold=\"true\"/>\n" +
                "    <text value=\"-\" repeat=\"fill\"/>\n" +
                "    <text value=\"收款明细\" condition=\"#{payments}\"/>\n" +
                "\n" +
                "    <group repeatKey=\"#{payments}\" condition=\"#{payments}\">\n" +
                "        <section>\n" +
                "            <text value=\"N0.#{js_num}    #{js_user}\"/>\n" +
                "            <text value=\" \" repeat=\"auto\"/>\n" +
                "            <text value=\"#{js_time}\"/>\n" +
                "        </section>\n" +
                "        <group repeatKey=\"#{_list}\">\n" +
                "            <text value=\"#{pay_name}\"/>\n" +
                "        </group>\n" +
                "    </group>\n" +
                "\n" +
                "    <text value=\"-\" repeat=\"fill\" condition=\"#{payments}\"/>\n" +
                "    <text value=\"电话: ${keys.tel}\"/>\n" +
                "    <text value=\"地址: ${keys.adds}\"/>\n" +
                "    <text value=\"打印时间: ${date}\" align=\"right\" marginTop=\"1\" marginBottom=\"1\"/>\n" +
                "</document>";
        PrintTemplate template = new PrintTemplate();
        String json = "{\n" +
                "    \"keys\": {\n" +
                "        \"mch\": \"天语雅阁\",\n" +
                "        \"tel\": \"0519-88877577\",\n" +
                "        \"adds\": \"常州市钟楼区五星街道勤业路295号（龙城天街1F-38、39）\",\n" +
                "        \"title\": \"结账单\",\n" +
                "        \"sn\": \"20200507125523222265\",\n" +
                "        \"table_name\": \"31台\",\n" +
                "        \"ctime\": \"2020-05-07 12:55\",\n" +
                "        \"guests\": 4,\n" +
                "        \"oper_name\": \"赵净净\",\n" +
                "        \"qrcode\": \"https://rc.you8858.com/rms/app_route/bill/100002/5BEFEF6BEFEB45B2BF48A525E8E2EA1A\",\n" +
                "        \"qrcode_tips\": \"微信扫码上方二维码，对本次消费评价\",\n" +
                "        \"sum_value\": \"数量：4  金额：143.2\",\n" +
                "        \"sl_sum\": \"4\",\n" +
                "        \"je_sum\": \"143.2\",\n" +
                "        \"wjje\": 0,\n" +
                "        \"check_in\": \"2020-05-07 12:55\"\n" +
                "    },\n" +
                "    \"goods\": [\n" +
                "        {\n" +
                "            \"unit\": \"35.8/位\",\n" +
                "            \"name\": \"团购价35.8\",\n" +
                "            \"num\": \"4\",\n" +
                "            \"price\": \"35.8\",\n" +
                "            \"amount\": \"143.2\",\n" +
                "            \"item_remark\": \"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"payments\": [\n" +
                "        {\n" +
                "            \"js_num\": \"1\",\n" +
                "            \"js_user\": \"赵净净\",\n" +
                "            \"js_time\": \"2020-05-07 14:42\",\n" +
                "            \"_list\": [\n" +
                "                {\n" +
                "                    \"pay_name\": \"美团：143.2\",\n" +
                "                    \"pay_remark\": \"\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Map map = JSONUtil.toMap(json);
        Document document = template.parser(tmpStr, map);
        for (Element element : document.getElements()) {
            byte[] bytes = ConverterKit.matchConverterToBytes(element, new Device(new Paper_80()));
        }
    }
}
