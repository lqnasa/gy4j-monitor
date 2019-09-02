package cn.gy4j.monitor.collector.util;

import java.util.Date;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class DateUtil {
    /**
     * 毫秒转Date.
     *
     * @param milliSecond 时间（毫秒）
     * @return
     */
    public static Date milliSecondToDate(long milliSecond) {
        Date date = new Date();
        date.setTime(milliSecond);
        return date;
    }
}
