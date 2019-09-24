package com.ciin.pos;

import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test {


    public static void main(String[] args) {
//        Printer printer = new NetworkPrinter("192.168.10.60");
//        String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
//        Template template = new Template(read);
//        PrintTask printTask = new PrintTask(template);
//        printer.print(printTask);

        String text = "hello hous";
        ByteBuffer buffer = new ByteBuffer();
        byte[] data = text.getBytes();
        int length = data.length;
        buffer.write(ByteUtils.intToByteArray(length));
        buffer.write(data);

        InputStream is = new ByteArrayInputStream(buffer.toByteArray());
        DataInputStream inputStream = new DataInputStream(is);
        try {
            int i = inputStream.readInt();
            System.out.println(i);
            byte[] bytes = new byte[i];
            inputStream.read(bytes);
            String s = new String(bytes);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}