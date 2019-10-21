package com.ciin.pos.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

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

    public static boolean isAdmin() {
        String[] groups = (new com.sun.security.auth.module.NTSystem()).getGroupIDs();
        for (String group : groups) {
            if (group.equals("S-1-5-32-544"))
                return true;
        }
        return false;
    }
}
