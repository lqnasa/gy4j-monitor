package cn.gy4j.monitor.sniffer.plugins.spring.resttemplate;

import cn.gy4j.monitor.sniffer.core.constant.Constants;
import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;
import cn.gy4j.monitor.sniffer.core.trace.Span;
import cn.gy4j.monitor.sniffer.core.trace.Tracer;
import cn.gy4j.monitor.sniffer.core.trace.TracerManager;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Method;
import java.net.URI;

import static cn.gy4j.monitor.sniffer.core.constant.Constants.Tag.TAG_ARGUMENTS;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class RestTemplateDoExecuteInterceptor implements InstMethodInterceptor {
    private static final ILogger logger = LoggerFactory.getLogger(RestTemplateDoExecuteInterceptor.class);

    @Override
    public Span beforeMethod(Method method, Object[] allArguments) {
        final URI requestUrl = (URI) allArguments[0];
        final HttpMethod httpMethod = (HttpMethod) allArguments[1];
        Tracer tracer = TracerManager.getOrCreate();
        Span span = tracer.buildSpan(requestUrl.getPath()).start()
                .setTag(Constants.Tag.TAG_HTTP_URL, requestUrl.getScheme() + "://" + requestUrl.getHost() + ":" + requestUrl.getPort() + requestUrl.getPath())
                .setTag(Constants.Tag.TAG_HTTP_METHOD, httpMethod.toString())
                .setTag(Constants.Tag.TAG_COMPONENT, "http")
                .setTag(TAG_ARGUMENTS, requestUrl.getQuery());
        return span;
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Object ret, Span span) {
        TracerManager.finish(span);
        return ret;
    }

    @Override
    public void handleMethodException(Method method, Object[] allArguments, Throwable t, Span span) {
        TracerManager.error(t, span);
    }
}
