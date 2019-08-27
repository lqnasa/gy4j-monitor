package cn.gy4j.monitor.sniffer.core.logging;

import cn.gy4j.monitor.sniffer.core.config.AgentConfig;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.logging.impl.NoopLogger;
import cn.gy4j.monitor.sniffer.core.logging.impl.SimpleLogger;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class LoggerFactory {
    /**
     * 获取日志对象.
     *
     * @param clazz 日志来源类
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
