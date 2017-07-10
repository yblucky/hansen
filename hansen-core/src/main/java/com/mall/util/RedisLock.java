package com.mall.util;

import com.mall.redis.Strings;
import org.apache.commons.lang3.StringUtils;

/**
 *  2017/4/24 0024.
 */
public class RedisLock {
    public static boolean redisLock(String key,String orderNo,int seconds) {
        String value = Strings.get(key);
        if (StringUtils.isEmpty(value)) {
            Strings.setEx(key, seconds, orderNo);
            return true;
        }
        return false;
    }
}
