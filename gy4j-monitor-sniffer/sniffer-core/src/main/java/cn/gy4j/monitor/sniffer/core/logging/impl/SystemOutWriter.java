package cn.gy4j.monitor.sniffer.core.logging.impl;

import cn.gy4j.monitor.sniffer.core.logging.api.IWriter;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public enum SystemOutWriter implements IWriter {
    INSTANCE;

    @Override
    public void write(String message) {
        System.out.println(message);
    }
}