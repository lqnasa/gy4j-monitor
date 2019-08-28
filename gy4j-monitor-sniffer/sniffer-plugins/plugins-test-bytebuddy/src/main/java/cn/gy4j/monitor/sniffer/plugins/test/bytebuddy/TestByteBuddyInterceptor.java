package cn.gy4j.monitor.sniffer.plugins.test.bytebuddy;

import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;
import cn.gy4j.monitor.sniffer.core.trace.Span;
import cn.gy4j.monitor.sniffer.core.trace.Tracer;
import cn.gy4j.monitor.sniffer.core.trace.TracerManager;

import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.gy4j.monitor.sniffer.core.constant.Constants.Tag.TAG_ARGUMENTS;
import static cn.gy4j.monitor.sniffer.core.constant.Constants.Tag.TAG_COMPONENT;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class TestByteBuddyInterceptor implements InstMethodInterceptor {
    @Override
    public Span beforeMethod(Method method, Object[] allArguments) {
        Tracer tracer = TracerManager.getOrCreate();
        Span span = tracer.buildSpan(method.getDeclaringClass().getName() + ":" + method.getName())
                .withTag(TAG_COMPONENT, "test")
                .withTag(TAG_ARGUMENTS, allArguments == null ? "" : Arrays.asList(allArguments).toString())
                .start();
        return span;
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Object ret, Span span) {
        TracerManager.finish(span);
        return ret;
    }

    @Override
    public void handleMethodException(Method method, Object[] allArguments, Throwable throwable, Span span) {
        TracerManager.error(throwable, span);
    }
}
