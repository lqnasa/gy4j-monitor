package cn.gy4j.monitor.sniffer.core.transport;


import cn.gy4j.monitor.sniffer.core.remote.RemoteEvent;

/**
 * 传输对象接口.
 *
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-09-30
 */
public interface Transportor {
    /**
     * 事件.
     *
     * @return
     */
    RemoteEvent getRemoteEvent();

    /**
     * 内容.
     *
     * @return
     */
    String getContent();
}
