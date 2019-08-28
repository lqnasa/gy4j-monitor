package cn.gy4j.monitor.sniffer.core.trace;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class SpanScopeManager {
    /**
     * 本地线程的Scope.
     */
    final ThreadLocal<SpanScope> scopeThreadLocal = new ThreadLocal<>();

    public Span activeSpan() {
        SpanScope scope = scopeThreadLocal.get();
        return scope == null ? null : scope.span;
    }

    public SpanScope activate(Span span) {
        return new SpanScope(this, span);
    }
}
