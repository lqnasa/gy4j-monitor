package cn.gy4j.monitor.sniffer.core.logging;

import cn.gy4j.monitor.sniffer.core.config.AgentConfig;
import cn.gy4j.monitor.sniffer.core.config.AgentConfigInitializer;
import cn.gy4j.monitor.sniffer.core.config.AgentPackagePath;
import cn.gy4j.monitor.sniffer.core.logging.api.IWriter;
import cn.gy4j.monitor.sniffer.core.logging.impl.FileWriter;
import cn.gy4j.monitor.sniffer.core.logging.impl.SystemOutWriter;
import cn.gy4j.monitor.sniffer.core.util.StringUtil;

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
        if (AgentConfig.Logging.TYPE == LoggerType.STDOUT || !AgentConfigInitializer.isInited()) {
            return SystemOutWriter.INSTANCE;
        } else {
            if (StringUtil.isEmpty(AgentConfig.Logging.PATH)) {
                AgentConfig.Logging.PATH = AgentPackagePath.getAgentDir().getAbsolutePath() + "/logs/";
            }
            return FileWriter.getInstance();
        }
    }
}
