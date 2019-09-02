package cn.gy4j.monitor.sniffer.plugins.spring.resttemplate;

import cn.gy4j.monitor.sniffer.core.constant.Constants;
import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;
import cn.gy4j.monitor.sniffer.core.trace.Span;
import cn.gy4j.monitor.sniffer.core.trace.SpanContext;
import cn.gy4j.monitor.sniffer.core.trace.Tracer;
import cn.gy4j.monitor.sniffer.core.trace.TracerManager;
import org.springframework.http.client.ClientHttpRequest;

import java.lang.reflect.Method;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class RestTemplateCreateRequestInterceptor implements InstMethodInterceptor {
    @Override
    public Span beforeMethod(Method method, Object[] allArguments) {
        return TracerManager.getOrCreate().activeSpan();
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Object ret, Span span) {
        ClientHttpRequest request = (ClientHttpRequest) ret;
        Tracer tracer = TracerManager.getOrCreate();
        if (tracer.activeSpan() != null && tracer.activeSpan().context() != null) {
            SpanContext context = tracer.activeSpan().context();
            String contextStr = tracer.formatCarrierContext(context);
            request.getHeaders().add(Constants.CARRIER_KEY, contextStr);
        }
        return ret;
    }

    @Override
    public void handleMethodException(Method method, Object[] allArguments, Throwable t, Span span) {

    }
}