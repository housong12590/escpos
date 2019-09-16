package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.device.DeviceFactory;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.io.IOException;

public class NetworkPrinter extends AbstractPrinter {

    private String host;
    private int port;
    private int timeout;
    private boolean isStop;
    private long mLastActivationTime;
    private Thread mPrinterThread;
    private boolean mPrinterThreadAlive;

    public NetworkPrinter(String host) {
        this(DeviceFactory.getDefault(), host);
    }

    public NetworkPrinter(Device device, String host) {
        this(device, host, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT);
    }

    public NetworkPrinter(Device device, String host, int port, int timeout) {
        super(device, new SocketConnection(host, port, timeout));
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    private void startPrintThread() {
        // 初始化打印机线程
        if (this.mPrinterThread == null) {
            this.mPrinterThread = new Thread(this);
            this.mPrinterThread.setName(this.mConnection.toString());
            LogUtils.debug("开启打印线程");
        }
        if (!this.mPrinterThread.isAlive()) {
            mPrinterThreadAlive = true;
            this.mPrinterThread.start();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    @Override
    public void print(PrintTask printTask) {
        if (isStop) {
            throw new RuntimeException("打印机已销毁, 无法执行新的打印任务");
        }
        if (!this.mPrinterThreadAlive) {
            startPrintThread();
        }
        printTask.setPrinter(this);
        printTaskQueue.offer(printTask);
        LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
    }


    @Override
    public boolean checkConnect() throws IOException {
        OrderSet orderSet = this.mDevice.getOrderSet();
        byte[] buff = new byte[48];
        int readLength = this.mConnection.writeAndRead(orderSet.status(), buff);
        return readLength != -1;
    }

    @Override
    public void release() {
        this.isStop = true;
        this.mPrinterThreadAlive = false;
        this.mPrinterThread = null;
        this.mConnection.close();
    }

    @Override
    public void run() {
        LogUtils.debug("执行打印循环");
        while (!isStop) {
            if (!this.mConnection.isConnect()) {
                retryConnection();
            } else if (!printTaskQueue.isEmpty()) {
                execPrintTask();
            } else if (System.currentTimeMillis() - mLastActivationTime > 10000) {
                mLastActivationTime = System.currentTimeMillis();
                try {
                    checkConnect();
                } catch (IOException e) {
                    retryConnection();
                }
            } else {
                Utils.sleep(200);
            }
        }
    }

    private void execPrintTask() {
        PrintTask printTask = printTaskQueue.peek();
        if (printTask == null) {
            return;
        }
        String taskId = printTask.getTaskId();
        if (printTask.isTimeOut()) { // 判断任务是否超时
            // 打印任务已超时 , 不再执行打印了
            printTaskQueue.poll();
            LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", taskId));
            if (mPrintTaskCallback != null) {
                // 打印超时回调
                mPrintTaskCallback.onError(this, printTask, "打印任务超时");
                return;
            }
        }
        try {
            // 检测打印机状态
            if (!checkConnect()) return;
            // 执行打印任务
            printTask.call();
            // 从打印列表里移除
            printTaskQueue.poll();
            // 刷新最后激活时间
            mLastActivationTime = System.currentTimeMillis();
            LogUtils.debug(String.format("%s 打印完成", taskId));
            if (mPrintTaskCallback != null) {
                // 打印完成回调
                mPrintTaskCallback.onSuccess(this, printTask);
            }
        } catch (Exception e) {
            if (e instanceof IOException) {
                // 连接异常, 执行重新连接
                retryConnection();
            } else {
                // 打印抛出现不可逆异常, 直接返回给调用方
                String errorMsg = String.format("打印失败, 错误原因: %s", e.getMessage());
                LogUtils.error(taskId + " " + errorMsg);
                if (mPrintTaskCallback != null) {
                    // 打印错误回调
                    mPrintTaskCallback.onError(this, printTask, errorMsg);
                }
            }
        }
        // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
        Utils.sleep(printTask.getIntervalTime());
    }

    // 重新连接
    private void retryConnection() {
        while (!this.mConnection.isConnect() && !isStop) {
            try {
                try {
                    // 建立打印机连接
                    this.mConnection.doConnect();
                } catch (IOException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("连接异常, 正在尝试重连  " + e.getMessage());
                        mPrinterErrorCallback.onConnectError(this, mConnection);
                    }
                    throw e;
                }
                try {
                    // 检测打印机是否可用
                    checkConnect();
                } catch (IOException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("打印机异常, 请重启打印机  " + e.getMessage());
                        mPrinterErrorCallback.onPrinterError(this);
                    }
                    throw e;
                }
                LogUtils.debug(" 连接成功");
            } catch (Exception ex) {
                Utils.sleep(5000);
            }
        }
    }

}
