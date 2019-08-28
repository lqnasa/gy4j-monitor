package cn.gy4j.monitor.sniffer.core.trace;

import cn.gy4j.monitor.sniffer.core.constant.Constants;
import cn.gy4j.monitor.sniffer.core.util.ExceptionUtil;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class TracerManager {
    /**
     * 本地线程的方式管理，暂时不考虑跨线程的情况.
     */
    private static ThreadLocal<Tracer> TRACER = new ThreadLocal<>();

    /**
     * 获取或创建Tracer对象.
     *
     * @return
     */
    public static Tracer getOrCreate() {
        Tracer tracer = TRACER.get();
        if (tracer != null) {
            return tracer;
        } else {
            tracer = new Tracer();
            TRACER.set(tracer);
            return tracer;
        }
    }

    /**
     * 结束当前的span.
     *
     * @param span span对象
     */
    public static void finish(Span span) {
        if (TRACER.get() != null && TRACER.get().activeSpan() != null && TRACER.get().activeSpan() == span) {
            span.finish();
        }
    }

    /**
     * 异常堆栈记录.
     *
     * @param throwable 异常对象
     * @param span      span对象
     */
    public static Span error(Throwable throwable, Span span) {
        if (TRACER.get() != null && TRACER.get().activeSpan() != null && TRACER.get().activeSpan() == span) {
            span.setTag(Constants.Tag.TAG_ERROR, true);
            span.setTag(Constants.Tag.TAG_ERROR_MESSAGE, throwable.getMessage());
            span.setTag(Constants.Tag.TAG_ERROR_STACK, ExceptionUtil.format(throwable));
            return span;
        }
        return null;
    }

    /**
     * 异常信息记录.
     *
     * @param message 异常信息
     * @param span    span对象
     */
    public static Span error(String message, Span span) {
        if (TRACER.get() != null && TRACER.get().activeSpan() != null && TRACER.get().activeSpan() == span) {
            span.setTag(Constants.Tag.TAG_ERROR, true);
            span.setTag(Constants.Tag.TAG_ERROR_MESSAGE, message);
            return span;
        }
        return null;
    }

    /**
     * 清除线程对象.
     */
    public static void clear() {
        TRACER.set(null);
    }
}
