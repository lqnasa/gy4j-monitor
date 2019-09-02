package cn.gy4j.monitor.sniffer.plugins.spring.mvc.annotation;

import cn.gy4j.monitor.sniffer.core.constant.Constants;
import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;
import cn.gy4j.monitor.sniffer.core.trace.*;
import cn.gy4j.monitor.sniffer.core.util.StringUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.gy4j.monitor.sniffer.core.constant.Constants.Tag.TAG_ARGUMENTS;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class ControllerInterceptor implements InstMethodInterceptor {
    private static final ILogger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Override
    public Span beforeMethod(Method method, Object[] allArguments) {
        Tracer tracer = TracerManager.getOrCreate();
        SpanContext spanContext = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        if (httpServletRequest == null) {
            return null;
        }
        if (tracer.activeSpan() == null) {
            // 尝试获取request里面的SpanContext
            String carrierContext = httpServletRequest.getHeader(Constants.CARRIER_KEY);
            if (StringUtil.isNotEmpty(carrierContext)) {
                spanContext = tracer.parseCarrierContext(carrierContext);
            }
        }
        String httpMethod = httpServletRequest.getMethod();
        String httpUrl = httpServletRequest.getRequestURI();
        SpanBuilder spanBuilder = tracer.buildSpan(httpUrl);
        if (spanContext != null) {
            spanBuilder = spanBuilder.asChildOf(spanContext);
        }
        Span span = spanBuilder.start().setTag(Constants.Tag.TAG_HTTP_METHOD, httpMethod)
                .setTag(Constants.Tag.TAG_HTTP_URL, httpUrl)
                .setTag(Constants.Tag.TAG_COMPONENT, "http")
                .setTag(TAG_ARGUMENTS, allArguments == null ? "" : Arrays.asList(allArguments).toString());
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
