package com.ciin.pos.printer;

import com.ciin.pos.connect.Connection;
import com.ciin.pos.device.Device;

import java.io.IOException;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;

public class DrivePrinter extends AbstractPrinter {

    private PrintService printService;

    public DrivePrinter(Device device, Connection connection) {
        super(device, connection);
    }

    private void findPrintService(String printName) {
        PrintServiceLookup.lookupPrintServices(null, null);
    }

    @Override
    public void print(PrintTask printTask) {
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        pras.add(MediaSizeName.ISO_A4);
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);


//        PrintService printService = getPrintService(printerName);
//        try {
//            DocPrintJob job = printService.createPrintJob();
//            InputStream is = new ByteArrayInputStream(file);
//            DocAttributeSet das = new HashDocAttributeSet();
//            Doc doc = new SimpleDoc(is, flavor, das);
//            job.print(doc, pras);
//        } catch (Exception e) {
//            log.error("print error, {}", e.getMessage(), e);
//            throw new RuntimeException("print error");
//        }
    }

    @Override
    public boolean checkConnect() throws IOException {
        return false;
    }

    @Override
    public void release() {

    }

    @Override
    public void run() {

    }
}
