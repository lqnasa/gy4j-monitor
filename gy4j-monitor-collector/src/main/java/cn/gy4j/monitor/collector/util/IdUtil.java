package cn.gy4j.monitor.collector.util;

import java.util.UUID;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class IdUtil {
    /**
     * 获取ID.
     *
     * @return
     */
    public static final String getId() {
        return UUID.randomUUID().toString();
    }
}
