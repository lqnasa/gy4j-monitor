package cn.gy4j.monitor.sniffer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class DateUtil {

    /**
     * 日期格式:年-月-日 时:分:秒:毫秒
     */
    private static final String DATEFORMAT_FULL = "yyyy-MM-dd HH:mm:ss:SSS";

    /**
     * 当前的毫秒
     *
     * @return
     */
    public static long getNowMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 日期转换为格式:年-月-日 时:分:秒:毫秒
     *
     * @param date 被转换的日期
     * @return 格式化的日期字符串
     */
    public static String getFullFormat(Date date) {
        return new SimpleDateFormat(DATEFORMAT_FULL).format(date);
    }
}
