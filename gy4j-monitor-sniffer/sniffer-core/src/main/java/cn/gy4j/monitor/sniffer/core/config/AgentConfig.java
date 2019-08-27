package cn.gy4j.monitor.sniffer.core.config;


import cn.gy4j.monitor.sniffer.core.logging.LoggerLevel;
import cn.gy4j.monitor.sniffer.core.logging.LoggerType;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class AgentConfig {
    public static class Logging {
        public static long MAX_FILE_SIZE = 1024 * 1024 * 200;
        public static LoggerLevel LEVEL = LoggerLevel.DEBUG;
        public static LoggerType TYPE = LoggerType.STDOUT;
        public static String PATH = "";
        public static String FILE_NAME = "sniffer-agent.log";
    }
}
