package com.ciin.pos;

import gnu.io.*;

import java.io.IOException;

public class Test {

    private static final String PING = "ping";
    private static final String PONG = "pong";


    public static void main(String[] args) throws InterruptedException, IOException, NoSuchPortException, PortInUseException {
//        Printer printer = new SerialPortPrinter(DeviceFactory.getDefault(), "COM4", 38400);
////        Printer printer = new DrivePrinter(DeviceFactory.getDefault(), "\\\\DESKTOP-GSJUGH8\\GP-C80300 Series");
//        for (int i = 0; i < 1; i++) {
//            String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
//            PrintTask printTask = new PrintTask(new Template(read));
//            printer.print(printTask);
//        }
//        Utils.sleep(10 * 1000);
//        printer.close();
    }
    //mvn install:install-file -Dfile=E:\code\java\escpos\libs\android.jar -DgroupId=com.android -DartifactId=android -Dversion=1.0.0 -Dpackaging=jar
}