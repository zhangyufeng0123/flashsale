package org.gotomove.flashsale.utils;

import java.util.UUID;

/**
 * @Author zhang
 * @Date 2024/1/24
 * @Description
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
