package com.ciin.pos.util;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemUtils {


    public static List<PrintService> getPrintServices() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        return new ArrayList<>(Arrays.asList(printServices));
    }

    public static PrintService getPrintService(String printerName) {
        if (printerName == null || printerName.equals("")) {
            return null;
        }
        for (PrintService printService : getPrintServices()) {
            if (printService.getName().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
}
