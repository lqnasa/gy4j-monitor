package cn.gy4j.monitor.sniffer.core.trace;

import cn.gy4j.monitor.sniffer.core.util.DateUtil;
import cn.gy4j.monitor.sniffer.core.util.SpanUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class SpanBuilder {
    /**
     * tracer对象.
     */
    private Tracer tracer;
    /**
     * Span的名称.
     */
    private String operationName;
    /**
     * 是否忽略激活标识.
     */
    private boolean ignore = false;
    /**
     * Span关系列表.
     */
    private List<SpanReference> references = new ArrayList<>();
    /**
     * Span初始化标签.
     */
    private Map<String, Object> initialTags = new HashMap<>();
    /**
     * Span创建时间.
     */
    private long startMicros;

    /**
     * 构造函数.
     *
     * @param tracer        tracer对象
     * @param operationName 操作名称
     */
    public SpanBuilder(Tracer tracer, String operationName) {
        this.tracer = tracer;
        this.operationName = operationName;
    }

    /**
     * 设置父spanContext..
     *
     * @param parent 父span的上下文context
     * @return
     */
    public SpanBuilder asChildOf(SpanContext parent) {
        return addReference(SpanReference.REFERENCES_CHILD_OF, parent);
    }

    /**
     * 设置父span.
     *
     * @param parent 父span
     * @return
     */
    public SpanBuilder asChildOf(Span parent) {
        if (parent == null) {
            return this;
        }
        return addReference(SpanReference.REFERENCES_CHILD_OF, parent.context());
    }

    /**
     * 添加span关联.
     *
     * @param referenceType     关联类型
     * @param referencedContext 关联的spanContext
     * @return
     */
    public SpanBuilder addReference(String referenceType, SpanContext referencedContext) {
        if (referencedContext != null) {
            this.references.add(new SpanReference(referencedContext, referenceType));
        }
        return this;
    }

    /**
     * 设置ignore为true，span的屏蔽标识.
     *
     * @return
     */
    public SpanBuilder ignore() {
        ignore = true;
        return this;
    }

    /**
     * 设置String标签.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public SpanBuilder withTag(String key, String value) {
        this.initialTags.put(key, value);
        return this;
    }

    /**
     * 设置Boolean标签.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public SpanBuilder withTag(String key, boolean value) {
        this.initialTags.put(key, value);
        return this;
    }

    /**
     * 设置Number标签.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public SpanBuilder withTag(String key, Number value) {
        this.initialTags.put(key, value);
        return this;
    }

    /**
     * 设置开始时间.
     *
     * @param microseconds 开始时间
     * @return
     */
    public SpanBuilder withStartTimestamp(long microseconds) {
        this.startMicros = microseconds;
        return this;
    }

    /**
     * 创建Span.
     *
     * @return
     */
    public Span start() {
        if (this.startMicros == 0) {
            this.startMicros = DateUtil.getNowMillis();
        }
        SpanContext activeSpanContext = activeSpanContext();
        if (references.isEmpty() && !ignore && activeSpanContext != null) {
            references.add(new SpanReference(activeSpanContext, SpanReference.REFERENCES_CHILD_OF));
        }
        if (SpanUtil.isIgnoreOperationName(operationName)) {
            tracer.setIgnore(true);
        }
        return new Span(tracer, operationName, startMicros, initialTags, references);
    }

    private SpanContext activeSpanContext() {
        Span span = tracer.activeSpan();
        if (span == null) {
            return null;
        }
        return span.context();
    }
}
