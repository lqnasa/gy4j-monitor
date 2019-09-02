package cn.gy4j.monitor.sniffer.plugins.spring.resttemplate;

import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;
import cn.gy4j.monitor.sniffer.core.trace.Span;
import cn.gy4j.monitor.sniffer.core.trace.TracerManager;
import org.springframework.http.client.ClientHttpResponse;

import java.lang.reflect.Method;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class RestTemplateHandleResponseInterceptor implements InstMethodInterceptor {

    @Override
    public Span beforeMethod(Method method, Object[] allArguments) {
        return TracerManager.getOrCreate().activeSpan();
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Object ret, Span span) throws Throwable {
        ClientHttpResponse response = (ClientHttpResponse) allArguments[2];
        int statusCode = response.getStatusCode().value();
        if (statusCode >= 400) {
            TracerManager.error("http.status code:" + statusCode, span);
        }
        return ret;
    }

    @Override
    public void handleMethodException(Method method, Object[] allArguments, Throwable t, Span span) {
        TracerManager.error(t, span);
    }
}
