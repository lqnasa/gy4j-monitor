package cn.gy4j.monitor.sniffer.core.logging.impl;

import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public enum NoopLogger implements ILogger {
    INSTANCE;

    @Override
    public void debug(String message) {

    }

    @Override
    public void debug(String format, Object... args) {

    }

    @Override
    public void info(String message) {

    }

    @Override
    public void info(String format, Object... args) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void warn(String format, Object... args) {

    }

    @Override
    public void warn(Throwable throwable, String format) {

    }

    @Override
    public void warn(Throwable throwable, String format, Object... args) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void error(String format, Object... args) {

    }

    @Override
    public void error(Throwable throwable, String message) {

    }

    @Override
    public void error(Throwable throwable, String format, Object... args) {

    }

    @Override
    public boolean isDebugEnable() {
        return false;
    }

    @Override
    public boolean isInfoEnable() {
        return false;
    }

    @Override
    public boolean isWarnEnable() {
        return false;
    }

    @Override
    public boolean isErrorEnable() {
        return false;
    }
}