package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;
import com.ciin.pos.util.SystemUtils;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import java.io.ByteArrayInputStream;

/**
 * 驱动打印机
 * 打印机驱动下载地址:
 * <p>
 * 佳博 : http://cn.gainscha.com/qudong.html
 * 爱普生: http://www.epson.com.cn/Apps/tech_support/GuideDrive.aspx?columnid=384&ptype=0&pmodel=0&strOs=
 * 北洋: http://www.snbctechs.com/companyfile/65.html
 */
public class DrivePrinter extends AbstractPrinter {

    private PrintService printService;
    private String printerName;

    public DrivePrinter(Device device, String printerName) {
        super(device);
        this.printerName = printerName;
        printService = SystemUtils.getPrintService(printerName);
    }

    @Override
    public void close() {
        printService = null;
    }


    @Override
    protected boolean print0(PrintTask printTask) throws Exception {
        byte[] data = printTask.printData();
        String jobName = StringUtils.isEmpty(printTask.getTitle()) ? printTask.getTaskId() : printTask.getTitle();
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        pras.add(MediaSizeName.ISO_A4);
        pras.add(new JobName(jobName, null));
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        if (printService == null) {
            throw new RuntimeException(String.format("%s 打印机不存在", printerName));
        }
        DocPrintJob printJob = printService.createPrintJob();
        HashDocAttributeSet das = new HashDocAttributeSet();
        LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        SimpleDoc doc = new SimpleDoc(is, flavor, das);
        printJob.print(doc, pras);
        return true;
    }
}
