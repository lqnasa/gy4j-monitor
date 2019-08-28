package cn.gy4j.monitor.sniffer.core.trace;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class SpanReference {
    public static final String REFERENCES_CHILD_OF = "child_of";
    public static final String REFERENCES_FOLLOWS_FROM = "follows_from";

    /**
     * Span上下文对象.
     */
    private SpanContext spanContext;
    /**
     * Span关系类型.
     */
    private String referenceType;

    public SpanReference(SpanContext spanContext, String referenceType) {
        this.spanContext = spanContext;
        this.referenceType = referenceType;
    }

    public SpanContext getSpanContext() {
        return spanContext;
    }

    public String getReferenceType() {
        return referenceType;
    }
}
