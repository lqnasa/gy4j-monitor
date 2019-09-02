package cn.gy4j.monitor.sniffer.core.trace;

import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class Tracer {
    private static final ILogger logger = LoggerFactory.getLogger(Tracer.class);

    /**
     * 完成的Span列表.
     */
    private final List<Span> finishedSpans = new ArrayList<>();
    /**
     * Scope管理.
     */
    private final SpanScopeManager scopeManager;
    /**
     * Tracer的关闭标识.
     */
    private boolean isClosed;

    public Tracer() {
        this(new SpanScopeManager());
    }

    public Tracer(SpanScopeManager scopeManager) {
        this.scopeManager = scopeManager;
    }

    public Span activeSpan() {
        return this.scopeManager.activeSpan();
    }

    public SpanScope activateSpan(Span span) {
        return this.scopeManager.activate(span);
    }

    public SpanBuilder buildSpan(String operationName) {
        return new SpanBuilder(this, operationName);
    }

    /**
     * 结束tracer.
     */
    public void close() {
        if (scopeManager.activeSpan() == null) {
            if (logger.isDebugEnable()) {
                logger.debug("Tracer.close:" + finishedSpans);
            }
            TracerManager.clear();
            this.isClosed = true;
            this.finishedSpans.clear();
        }
    }

    /**
     * 添加完成的span.
     *
     * @param span span对象
     */
    public synchronized void appendFinishedSpan(Span span) {
        if (isClosed) {
            return;
        }
        this.finishedSpans.add(span);
    }
    /**
     * 把spanContext转换为Carrier的json串.
     *
     * @param spanContext spanContext对象
     * @return
     */
    public String formatCarrierContext(SpanContext spanContext) {
        SpanContext.Carrier carrier = new SpanContext.Carrier();
        carrier.setSpanId(spanContext.getSpanId());
        carrier.setTraceId(spanContext.getTraceId());
        carrier.setBaggage(spanContext.getBaggage());
        return GsonUtil.objectToJson(carrier);
    }

    /**
     * Carrier的json串转spanContext对象.
     *
     * @param carrierContext Carrier的json串
     * @return
     */
    public SpanContext parseCarrierContext(String carrierContext) {
        SpanContext.Carrier carrier = GsonUtil.jsonToObject(carrierContext, SpanContext.Carrier.class);
        return new SpanContext(carrier);
    }
}
