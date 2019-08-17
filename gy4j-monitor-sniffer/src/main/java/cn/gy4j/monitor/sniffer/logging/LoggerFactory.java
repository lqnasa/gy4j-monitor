package cn.gy4j.monitor.sniffer.logging;

import cn.gy4j.monitor.sniffer.config.AgentConfig;
import cn.gy4j.monitor.sniffer.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.logging.impl.NoopLogger;
import cn.gy4j.monitor.sniffer.logging.impl.SimpleLogger;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class LoggerFactory {
    /**
     * 获取日志对象
     *
     * @param clazz
     * @return
     */
    public static ILogger getLogger(Class<?> clazz) {
        if (AgentConfig.Logging.LEVEL == LoggerLevel.OFF) {
            return NoopLogger.INSTANCE;
        } else {
            return new SimpleLogger(clazz);
        }
    }
}
