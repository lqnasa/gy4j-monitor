package cn.gy4j.monitor.sniffer.core.trace;

import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.util.DateUtil;
import cn.gy4j.monitor.sniffer.core.util.IdUtil;
import cn.gy4j.monitor.sniffer.core.util.StringUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.gy4j.monitor.sniffer.core.constant.Constants.Baggage.BAGGAGE_ORDER;
import static cn.gy4j.monitor.sniffer.core.constant.Constants.Tag.TAG_ORDER;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class Span {
    private static final ILogger logger = LoggerFactory.getLogger(Span.class);

    /**
     * Span上下文对象.
     */
    private SpanContext context;
    /**
     * Span所在的Tracer.
     */
    private Tracer tracer;
    /**
     * Span标签.
     */
    private Map<String, Object> tags;
    /**
     * Span的Scope对象.
     */
    private SpanScope scope;
    /**
     * Span关系列表.
     */
    private List<SpanReference> references;
    /**
     * 父Span的id.
     */
    private String parentId;
    /**
     * Span的名称.
     */
    private String operationName;
    /**
     * Span的开始时间.
     */
    private long startMicros;
    /**
     * Span的结束时间.
     */
    private long finishMicros;
    /**
     * Span是否结束.
     */
    private boolean finished;

    /**
     * 构造函数.
     *
     * @param tracer        trace对象
     * @param operationName 操作名称
     * @param startMicros   开始时间
     * @param initialTags   初始化标签集
     * @param references    关系列表
     */
    public Span(Tracer tracer, String operationName, long startMicros, Map<String, Object> initialTags, List<SpanReference> references) {
        this.tracer = tracer;
        this.operationName = operationName;
        this.startMicros = startMicros;
        if (initialTags == null) {
            this.tags = new HashMap<>();
        } else {
            this.tags = new HashMap<>(initialTags);
        }
        if (references == null) {
            this.references = Collections.emptyList();
        } else {
            this.references = references;
        }
        SpanContext parent = findPreferredParentRef(this.references);
        if (parent == null) {
            this.context = new SpanContext(IdUtil.getId(), IdUtil.getId(), mergeBaggages(this.references));
            int order = 0;
            this.context.setBaggageItem(BAGGAGE_ORDER, order + "");
            this.tags.put(TAG_ORDER, order);
            this.parentId = null;
        } else {
            this.context = new SpanContext(parent.getTraceId(), IdUtil.getId(), mergeBaggages(this.references));
            String val = parent.getBaggageItem(BAGGAGE_ORDER);
            int order = 0;
            if (!StringUtil.isEmpty(val)) {
                order = Integer.valueOf(val) + 1;
            }
            this.context.setBaggageItem(BAGGAGE_ORDER, order + "");
            this.tags.put(TAG_ORDER, order);
            this.parentId = parent.getSpanId();
        }
        scope = tracer.activateSpan(this);
    }

    /**
     * 获取span的上下文SpanContext.
     *
     * @return
     */
    public SpanContext context() {
        return this.context;
    }

    /**
     * 设置String标签.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public Span setTag(String key, String value) {
        tags.put(key, value);
        return this;
    }

    /**
     * 设置Boolean标签.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public Span setTag(String key, boolean value) {
        tags.put(key, value);
        return this;
    }

    /**
     * 设置Number标签.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public Span setTag(String key, Number value) {
        tags.put(key, value);
        return this;
    }

    /**
     * 设置上下文传递对象BaggageItem的数据.
     *
     * @param key   key值
     * @param value value值
     * @return
     */
    public Span setBaggageItem(String key, String value) {
        this.context.setBaggageItem(key, value);
        return this;
    }

    /**
     * 根据key获取上下文传递对象BaggageItem的值.
     *
     * @param key key值
     * @return
     */
    public String getBaggageItem(String key) {
        return this.context.getBaggageItem(key);
    }

    /**
     * 设置span的操作名称.
     *
     * @param operationName 操作名称
     * @return
     */
    public Span setOperationName(String operationName) {
        this.operationName = operationName;
        return this;
    }

    /**
     * 结束span.
     */
    public void finish() {
        this.finish(DateUtil.getNowMillis());
    }

    /**
     * 结束span，带结束时间.
     *
     * @param finishMicros 结束时间
     */
    public void finish(long finishMicros) {
        if (logger.isDebugEnable()) {
            logger.debug("Span.finish:" + this);
        }
        scope.close();
        this.finishMicros = finishMicros;
        this.tracer.appendFinishedSpan(this);
        this.finished = true;
        tracer.close();
    }

    /**
     * 根据关系列表查找父Span的上下文对象.
     *
     * @param references span的依赖列表
     * @return
     */
    private static SpanContext findPreferredParentRef(List<SpanReference> references) {
        if (references.isEmpty()) {
            return null;
        }
        for (SpanReference reference : references) {
            if (SpanReference.REFERENCES_CHILD_OF.equals(reference.getReferenceType())) {
                return reference.getSpanContext();
            }
        }
        return references.get(0).getSpanContext();
    }

    /**
     * 合并已存在Span上下文的baggage信息，传递到下一个Span上下文.
     *
     * @param references span的依赖列表
     * @return
     */
    private static Map<String, String> mergeBaggages(List<SpanReference> references) {
        Map<String, String> baggage = new HashMap<>();
        for (SpanReference ref : references) {
            if (ref.getSpanContext().getBaggage() != null) {
                baggage.putAll(ref.getSpanContext().getBaggage());
            }
        }
        return baggage;
    }

    /**
     * 获取span的操作名称.
     *
     * @return
     */
    public String getOperationName() {
        return this.operationName;
    }

    @Override
    public String toString() {
        return "Span{" +
                "context=" + context +
                ", parentId='" + parentId + '\'' +
                ", operationName='" + operationName + '\'' +
                '}';
    }
}
