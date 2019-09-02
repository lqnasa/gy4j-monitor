package cn.gy4j.monitor.collector.util;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class IntegerUtil {
    /**
     * 转整数.
     *
     * @param val 值对象
     * @return
     */
    public static Integer valueOf(Object val) {
        if (val == null) {
            return 0;
        }
        if (val instanceof Double) {
            Double d = (Double) val;
            return d.intValue();
        }
        return Integer.valueOf(val.toString());
    }
}
