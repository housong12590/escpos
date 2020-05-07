package com.ciin.pos;


import com.ciin.pos.device.Device;
import com.ciin.pos.listener.OnPrintEventListener;
import com.ciin.pos.listener.PrintEvent;
import com.ciin.pos.parser.Template;
import com.ciin.pos.printer.NetworkPrinter;
import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;

public class Test {

    public static void main(String[] args) {
        //mvn install:install-file -Dfile=F:\android-sdk\platforms\android-29\android.jar -DgroupId=com.android -DartifactId=android -Dversion=1.1.0 -Dpackaging=jar
        NetworkPrinter printer = new NetworkPrinter(Device.getDefault(), "192.168.10.22");
        PrintTask printTask = new PrintTask(new Template(""));
        printTask.setPrintEventListener(new OnPrintEventListener() {
            @Override
            public void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj) {

            }
        });
        printer.print(printTask, true);
        printer.close();
    }
}