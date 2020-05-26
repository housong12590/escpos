package com.xiaom.pos4j.util;


import com.xiaom.pos4j.comm.SnowFlake;

import java.util.UUID;

/**
 * @author hous
 */
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
