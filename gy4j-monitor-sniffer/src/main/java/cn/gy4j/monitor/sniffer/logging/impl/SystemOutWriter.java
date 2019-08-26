package cn.gy4j.monitor.sniffer.logging.impl;

import cn.gy4j.monitor.sniffer.logging.api.IWriter;

/**
 * 基于str标准输出的Writer.
 * <p>
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