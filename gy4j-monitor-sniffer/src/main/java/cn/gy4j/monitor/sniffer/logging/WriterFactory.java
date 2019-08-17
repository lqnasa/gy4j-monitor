package cn.gy4j.monitor.sniffer.logging;

import cn.gy4j.monitor.sniffer.config.AgentConfig;
import cn.gy4j.monitor.sniffer.logging.api.IWriter;
import cn.gy4j.monitor.sniffer.logging.impl.FileWriter;
import cn.gy4j.monitor.sniffer.logging.impl.SystemOutWriter;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class WriterFactory {
    /**
     * 获取日志输出对象.
     *
     * @return 日志输出对象
     */
    public static IWriter getWriter() {
        if (AgentConfig.Logging.TYPE == LoggerType.STDOUT) {
            return SystemOutWriter.INSTANCE;
        } else {
            return FileWriter.getInstance();
        }
    }
}
