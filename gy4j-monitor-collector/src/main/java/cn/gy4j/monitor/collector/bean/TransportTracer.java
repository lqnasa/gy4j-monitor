package cn.gy4j.monitor.collector.bean;

import lombok.Data;

import java.util.List;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Data
public class TransportTracer {
    private String serverName;
    private String serverInstanceId;
    private String hostname;
    private String ip;
    private List<TransportSpan> spans;
}
