package cn.gy4j.monitor.sniffer.core.transport;

import cn.gy4j.monitor.sniffer.core.remote.RemoteEvent;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class TransportEntity {
    private RemoteEvent remoteEvent;
    private String content;

    public RemoteEvent getRemoteEvent() {
        return remoteEvent;
    }

    public void setRemoteEvent(RemoteEvent remoteEvent) {
        this.remoteEvent = remoteEvent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
