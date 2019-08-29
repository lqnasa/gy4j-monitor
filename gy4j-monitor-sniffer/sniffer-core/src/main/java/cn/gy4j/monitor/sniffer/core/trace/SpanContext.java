package cn.gy4j.monitor.sniffer.core.trace;

import java.util.Map;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class SpanContext {
    /**
     * Span上下文需要传递的信息.
     */
    private Map<String, String> baggage;
    /**
     * Span的id.
     */
    private String spanId;
    /**
     * Tracer的id.
     */
    private String traceId;

    /**
     * 构造函数.
     *
     * @param traceId tracer的id
     * @param spanId  span的id
     * @param baggage spanContext的传递信息
     */
    public SpanContext(String traceId, String spanId, Map<String, String> baggage) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.baggage = baggage;

    }

    /**
     * 获取traceId.
     *
     * @return
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * 获取spanId.
     *
     * @return
     */
    public String getSpanId() {
        return spanId;
    }

    /**
     * 获取spanContext的传递集合迭代器.
     *
     * @return
     */
    public Iterable<Map.Entry<String, String>> baggageItems() {
        return baggage.entrySet();
    }

    /**
     * 获取spanContext的传递集合.
     *
     * @return
     */
    public Map<String, String> getBaggage() {
        return baggage;
    }

    /**
     * 设置spanContext的传递信息.
     *
     * @param key key值
     * @param val value值
     * @return
     */
    public SpanContext setBaggageItem(String key, String val) {
        this.getBaggage().put(key, val);
        return this;
    }

    /**
     * 获取spanContext的传递信息.
     *
     * @param key key值
     * @return
     */
    public String getBaggageItem(String key) {
        return this.baggage.get(key);
    }

    @Override
    public String toString() {
        return "SpanContext{" +
                "baggage=" + baggage +
                ", spanId='" + spanId + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
