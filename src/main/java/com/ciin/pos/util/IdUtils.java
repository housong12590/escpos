package com.ciin.pos.util;


import com.ciin.pos.common.SnowFlake;

import java.util.UUID;

public class IdUtils {

    private static SnowFlake snowFlake = new SnowFlake(1, 1);

    public static void initSnowFlake(int dataCenterId, int machineId) {
        snowFlake = new SnowFlake(dataCenterId, machineId);
    }

    public static String generateId() {
        return String.valueOf(snowFlake.nextId());
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
}
