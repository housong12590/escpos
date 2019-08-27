package com.cin.pos.printer;

import java.util.concurrent.Callable;

public class PrintTask {

    private String taskId;

    private Callable<PrintTask> callable;

    public PrintTask(Callable<PrintTask> callable) {
        this.callable = callable;
        callable = new Callable<PrintTask>() {
            @Override
            public PrintTask call() throws Exception {
                return null;
            }
        };
    }
}
