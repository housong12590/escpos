package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;

import javax.print.*;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.print.Book;
import java.io.ByteArrayInputStream;

/**
 * 驱动打印机
 */
public class DrivePrinter extends AbstractPrinter {

    //https://blog.csdn.net/u012854263/article/details/51137097

    //https://blog.csdn.net/qq_38418296/article/details/80988887  驱动打印demo

    private PrintService printService;
    private String printerName;

    public DrivePrinter(Device device, String printerName) {
        super(device);
        this.printerName = printerName;
        printService = findPrintService(printerName);
    }

    private PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : printServices) {
            if (service.getName().equals(printerName)) {
                return service;
            }
        }
        return null;
    }


    @Override
    public void release() {
        printService = null;
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        byte[] printData = printTask.printData();
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        pras.add(MediaSizeName.ISO_A4);
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        if (printService == null) {
            throw new RuntimeException("没有相应的打印机");
        }
        DocPrintJob printJob = printService.createPrintJob();
        HashDocAttributeSet das = new HashDocAttributeSet();
        ByteArrayInputStream is = new ByteArrayInputStream(printData);
        SimpleDoc doc = new SimpleDoc(is, flavor, das);
        try {
            printJob.print(doc, pras);
        } catch (PrintException e) {
            e.printStackTrace();
        }

        Book book = new Book();
//        book.append();
        return false;
    }
}
