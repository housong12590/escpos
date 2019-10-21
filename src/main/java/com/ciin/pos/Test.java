package com.ciin.pos;


import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.parser.Template;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.FileUtils;
import com.ciin.pos.util.LogUtils;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        test02();
    }

    private static void test01() {
//        ForwardingPrinter printer = new ForwardingPrinter(Device.getDefault(), "127.0.0.1",
//                "驱动/GP-C80300 Series");
//        printer.testPrint();

        Template template = new Template(Constants.TEST_TEMPLATE);
        try {
            Device device = Device.getDefault();
            OrderSet orderSet = device.getOrderSet();
            byte[] bytes = template.toDocument().toBytes(device);
            ByteBuffer buffer = new ByteBuffer();
            buffer.write(bytes);
            buffer.write(orderSet.paperFeed(5));
            buffer.write(orderSet.cutPaper());
            byte[] data = buffer.toByteArray();
            LogUtils.debug("写入字节长度: " + data.length);
            File file = new File("D:\\work\\python\\Parallel\\input");
            FileUtils.fileWrite(file, data);
        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
    }

    private static void test02() {
        try {
            Process p = Runtime.getRuntime().exec("D:\\work\\python\\Parallel\\dist\\main.exe");
            p.waitFor();
            int i = p.exitValue();
            System.out.println("exit code : " + i);
            System.out.println("执行完成");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("执行失败");
        }
    }
}