package cn.gy4j.monitor.sniffer.logging.impl;

import cn.gy4j.monitor.sniffer.config.AgentConfig;
import cn.gy4j.monitor.sniffer.logging.LoggerLevel;
import cn.gy4j.monitor.sniffer.logging.WriterFactory;
import cn.gy4j.monitor.sniffer.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.util.DateUtil;
import cn.gy4j.monitor.sniffer.util.ExceptionUtil;
import cn.gy4j.monitor.sniffer.util.StringUtil;

import java.util.Date;
import java.util.regex.Matcher;

/**
 * 常规日志实现.
 * <p>
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class SimpleLogger implements ILogger {

    private Class<?> targetClass;

    /**
     * 构造函数.
     *
     * @param targetClass 日志来源类
     */
    public SimpleLogger(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public void debug(String message) {
        if (isDebugEnable()) {
            logger(LoggerLevel.DEBUG, message, null);
        }
    }

    @Override
    public void debug(String format, Object... args) {
        if (isDebugEnable()) {
            logger(LoggerLevel.DEBUG, replaceParam(format, args), null);
        }
    }

    @Override
    public void info(String message) {
        if (isInfoEnable()) {
            logger(LoggerLevel.INFO, message, null);
        }
    }

    @Override
    public void info(String format, Object... args) {
        if (isInfoEnable()) {
            logger(LoggerLevel.INFO, replaceParam(format, args), null);
        }
    }

    @Override
    public void warn(String message) {
        if (isWarnEnable()) {
            logger(LoggerLevel.WARN, message, null);
        }
    }

    @Override
    public void warn(String format, Object... args) {
        if (isWarnEnable()) {
            logger(LoggerLevel.WARN, replaceParam(format, args), null);
        }
    }

    @Override
    public void warn(Throwable t, String message) {
        if (isWarnEnable()) {
            logger(LoggerLevel.WARN, message, t);
        }
    }

    @Override
    public void warn(Throwable t, String format, Object... args) {
        if (isWarnEnable()) {
            logger(LoggerLevel.WARN, replaceParam(format, args), t);
        }
    }

    @Override
    public void error(String message) {
        if (isErrorEnable()) {
            logger(LoggerLevel.ERROR, message, null);
        }
    }

    @Override
    public void error(String format, Object... args) {
        if (isErrorEnable()) {
            logger(LoggerLevel.ERROR, replaceParam(format, args), null);
        }
    }

    @Override
    public void error(Throwable t, String message) {
        if (isErrorEnable()) {
            logger(LoggerLevel.ERROR, message, t);
        }
    }

    @Override
    public void error(Throwable t, String format, Object... args) {
        if (isErrorEnable()) {
            logger(LoggerLevel.ERROR, replaceParam(format, args), t);
        }
    }

    @Override
    public boolean isDebugEnable() {
        return LoggerLevel.DEBUG.compareTo(AgentConfig.Logging.LEVEL) >= 0;
    }

    @Override
    public boolean isInfoEnable() {
        return LoggerLevel.INFO.compareTo(AgentConfig.Logging.LEVEL) >= 0;
    }

    @Override
    public boolean isWarnEnable() {
        return LoggerLevel.WARN.compareTo(AgentConfig.Logging.LEVEL) >= 0;
    }

    @Override
    public boolean isErrorEnable() {
        return LoggerLevel.ERROR.compareTo(AgentConfig.Logging.LEVEL) >= 0;
    }


    /**
     * 日志输出公共方法.
     *
     * @param loggerLevel 日志级别
     * @param message     日志消息
     * @param throwable   异常
     */
    private void logger(LoggerLevel loggerLevel, String message, Throwable throwable) {
        WriterFactory.getWriter().write(format(loggerLevel, message, throwable));
    }

    /**
     * 日志消息格式化.
     *
     * @param loggerLevel 日志级别
     * @param message     日志消息
     * @param throwable   异常
     * @return
     */
    private String format(LoggerLevel loggerLevel, String message, Throwable throwable) {
        return StringUtil.join(' ', loggerLevel.name(), DateUtil.getFullFormat(new Date()),
                Thread.currentThread().getName(), targetClass.getSimpleName(), ": ",
                message, throwable == null ? "" : ExceptionUtil.format(throwable));
    }

    /**
     * 日志格式化转换:按{}的顺序用args列表替换.
     *
     * @param format 日志格式字符串
     * @param args   参数列表
     * @return
     */
    private String replaceParam(String format, Object... args) {
        int startSize = 0;
        int paramIndex = 0;
        int index;
        String tmpMessage = format;
        while ((index = format.indexOf("{}", startSize)) != -1) {
            if (paramIndex >= args.length) {
                break;
            }
            /*
             * @Fix Matcher.quoteReplacement:the Illegal group reference issue.
             * exp:"{}".replaceFirst("\\{\\}", "x$")
             */
            tmpMessage = tmpMessage.replaceFirst("\\{\\}", Matcher.quoteReplacement(String.valueOf(args[paramIndex++])));
            startSize = index + 2;
        }
        return tmpMessage;
    }
}
