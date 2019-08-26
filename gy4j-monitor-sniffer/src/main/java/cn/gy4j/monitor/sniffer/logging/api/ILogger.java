package cn.gy4j.monitor.sniffer.logging.api;

/**
 * 日志接口.
 * <p>
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public interface ILogger {
    void debug(String message);

    void debug(String format, Object... args);

    void info(String message);

    void info(String format, Object... args);

    void warn(String message);

    void warn(String format, Object... args);

    void warn(Throwable throwable, String format);

    void warn(Throwable throwable, String format, Object... args);

    void error(String message);

    void error(String format, Object... args);

    void error(Throwable throwable, String message);

    void error(Throwable throwable, String format, Object... args);

    boolean isDebugEnable();

    boolean isInfoEnable();

    boolean isWarnEnable();

    boolean isErrorEnable();
}
