package cn.gy4j.monitor.sniffer.core.trace;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class SpanScope {
    /**
     * Scope管理对象.
     */
    final SpanScopeManager scopeManager;
    /**
     * Span对象.
     */
    final Span span;
    /**
     * 上一个Scope对象.
     */
    final SpanScope preScope;

    /**
     * 构造函数.
     *
     * @param scopeManager span的scope管理对象
     * @param span         span对象
     */
    public SpanScope(SpanScopeManager scopeManager, Span span) {
        this.scopeManager = scopeManager;
        this.span = span;
        this.preScope = scopeManager.scopeThreadLocal.get();
        scopeManager.scopeThreadLocal.set(this);
    }


    /**
     * 结束spanScope.
     */
    public void close() {
        if (scopeManager.scopeThreadLocal.get() != this) {
            return;
        }
        scopeManager.scopeThreadLocal.set(preScope);
    }
}
