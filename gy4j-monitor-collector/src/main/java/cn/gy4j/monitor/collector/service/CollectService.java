package cn.gy4j.monitor.collector.service;

import cn.gy4j.monitor.collector.bean.TransportTracer;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public interface CollectService {
    /**
     * 保存transportTracer对象.
     *
     * @param transportTracer 分布式采集对象
     */
    void saveTracer(TransportTracer transportTracer) throws Exception;
}
