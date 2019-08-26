package cn.gy4j.monitor.sniffer.config;

import cn.gy4j.monitor.sniffer.logging.LoggerLevel;
import cn.gy4j.monitor.sniffer.logging.LoggerType;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class AgentConfig {
    public static class Logging {
        // 日志文件大小，超过该大小自动重命名
        public static long MAX_FILE_SIZE = 1024 * 1024 * 200;
        // 日志级别
        public static LoggerLevel LEVEL = LoggerLevel.DEBUG;
        // 输出模式
        public static LoggerType TYPE = LoggerType.FILE;
        // 日志文件存放目录
        public static String PATH = "";
        // 日志文件名
        public static String FILE_NAME = "sniffer-agent.log";
    }
}
