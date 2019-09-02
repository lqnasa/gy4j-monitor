package cn.gy4j.monitor.collector.bean;

import lombok.Data;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Data
public class TransportSpan {
    private String operationName;
    private String spanId;
    private String parentId;
    private String traceId;
    private String tags;
    private long startTime;
    private long finishTime;
    private String referenceType;
}
