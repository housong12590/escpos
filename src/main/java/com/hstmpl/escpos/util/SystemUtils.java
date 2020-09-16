package com.hstmpl.escpos.util;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author hous
 */
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

//    public static boolean isWindowsAdmin() {
//        String[] groups = (new NTSystem()).getGroupIDs();
//        for (String group : groups) {
//            if (group.equals("S-1-5-32-544"))
//                return true;
//        }
//        return false;
//    }

    public static String machineSerialId() {
        String str = getHardDiskSerialNumber() + getCPUSerialNumber();
        return StringUtils.md5(str);
    }

    private static String getHardDiskSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            return sc.next();
        } catch (Exception e) {
            return null;
        }
    }

    private static String getCPUSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            return sc.next();
        } catch (Exception e) {
            return null;
        }
    }
}
