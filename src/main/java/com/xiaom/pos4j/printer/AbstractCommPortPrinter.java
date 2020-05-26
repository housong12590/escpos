package com.xiaom.pos4j.printer;

import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.util.IOUtils;
import com.xiaom.pos4j.util.ListUtils;
import com.xiaom.pos4j.util.LogUtils;
import com.xiaom.pos4j.util.ThreadUtils;
import gnu.io.CommPort;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author hous
 */
public abstract class AbstractCommPortPrinter extends AbstractPrinter {

    private int bufferSize;
    private CommPort commPort;
    private OutputStream os;

    public AbstractCommPortPrinter(Device device) {
        super(device);
        bufferSize = Constants.BUFFER_SIZE;
    }

    @Override
    protected boolean print0(PrintTask printTask) throws Exception {
        if (!available()) {
            return false;
        }

        byte[] data = printTask.printData();
        LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
        try {
            for (byte[] bytes : ListUtils.splitArray(data, bufferSize)) {
                os.write(bytes);
                os.flush();
                ThreadUtils.sleep(30);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    abstract CommPort openPort() throws Exception;


    @Override
    public boolean available() {
        if (commPort == null) {
            try {
                commPort = openPort();
                os = commPort.getOutputStream();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void printEnd0() {
        if (commPort != null) {
            LogUtils.debug("关闭端口: " + this.commPort.getName());
            IOUtils.safeClose(os);
            commPort.close();
            commPort = null;
        }
    }
}
