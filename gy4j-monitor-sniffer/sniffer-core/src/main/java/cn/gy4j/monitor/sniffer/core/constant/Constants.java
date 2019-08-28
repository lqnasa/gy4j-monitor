package cn.gy4j.monitor.sniffer.core.constant;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class Constants {
    /**
     * 换行符.
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    public static class Tag {
        public static final String TAG_COMPONENT = "component";
        public static final String TAG_ERROR = "error";
        public static final String TAG_ERROR_MESSAGE = "errorMessage";
        public static final String TAG_ERROR_STACK = "errorStack";
        public static final String TAG_ORDER = "order";
        public static final String TAG_ARGUMENTS = "arguments";
    }


    public static class Baggage {
        public static final String BAGGAGE_ORDER = "order";
    }
}
