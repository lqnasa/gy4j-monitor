package cn.gy4j.monitor.sniffer.core.logging.api;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public interface IWriter {
    /**
     * 输出日志.
     *
     * @param message  日志内容
     */
    void write(String message);
}
