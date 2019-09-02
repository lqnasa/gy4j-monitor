package cn.gy4j.monitor.sniffer.core.transport;

import cn.gy4j.monitor.sniffer.core.config.AgentConfig;
import cn.gy4j.monitor.sniffer.core.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class TransportTracer {
    private String serverName;
    private String serverInstanceId;
    private String hostname;
    private String ip;
    private List<TransportSpan> spans;

    /**
     * 构造函数.
     */
    public TransportTracer() {
        this.setServerName(AgentConfig.Agent.SERVER_NAME);
        this.setServerInstanceId(AgentConfig.Agent.SERVER_INSTANCE_ID);
        this.setHostname(AgentConfig.Agent.HOSTNAME);
        this.setIp(AgentConfig.Agent.IP);
        this.spans = new ArrayList<>();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerInstanceId() {
        return serverInstanceId;
    }

    public void setServerInstanceId(String serverInstanceId) {
        this.serverInstanceId = serverInstanceId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<TransportSpan> getSpans() {
        return spans;
    }

    public void setSpans(List<TransportSpan> spans) {
        this.spans = spans;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return GsonUtil.objectToJson(this);
    }
}
