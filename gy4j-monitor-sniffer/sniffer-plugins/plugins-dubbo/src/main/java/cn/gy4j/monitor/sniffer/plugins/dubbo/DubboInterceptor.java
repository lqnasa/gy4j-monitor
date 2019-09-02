package cn.gy4j.monitor.sniffer.plugins.dubbo;

import cn.gy4j.monitor.sniffer.core.constant.Constants;
import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;
import cn.gy4j.monitor.sniffer.core.trace.*;
import cn.gy4j.monitor.sniffer.core.util.StringUtil;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;

import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.gy4j.monitor.sniffer.core.constant.Constants.Tag.TAG_ARGUMENTS;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class DubboInterceptor implements InstMethodInterceptor {

    @Override
    public Span beforeMethod(Method method, Object[] allArguments) {
        Invoker invoker = (Invoker) allArguments[0];
        RpcContext rpcContext = RpcContext.getContext();
        boolean isConsumer = rpcContext.isConsumerSide();
        URL requestUrl = invoker.getUrl();
        Invocation invocation = (Invocation) allArguments[1];
        final String host = requestUrl.getHost();
        final int port = requestUrl.getPort();
        Tracer tracer = TracerManager.getOrCreate();
        Span span;
        if (isConsumer) {
            span = tracer.buildSpan(generateOperationName(requestUrl, invocation)).start();
            SpanContext context = tracer.activeSpan().context();
            String contextStr = tracer.formatCarrierContext(context);
            rpcContext.getAttachments().put(Constants.CARRIER_KEY, contextStr);
        } else {
            SpanContext spanContext = null;
            if (tracer.activeSpan() == null) {
                String carrierContext = rpcContext.getAttachments().get(Constants.CARRIER_KEY);
                if (StringUtil.isNotEmpty(carrierContext)) {
                    spanContext = tracer.parseCarrierContext(carrierContext);
                }
            }
            SpanBuilder spanBuilder = tracer.buildSpan(generateOperationName(requestUrl, invocation));
            if (spanContext != null) {
                spanBuilder = spanBuilder.asChildOf(spanContext);
            }
            span = spanBuilder.start();
        }
        span.setTag(Constants.Tag.TAG_RPC_URL, generateRequestUrl(requestUrl, invocation))
                .setTag(Constants.Tag.TAG_PEER_ADDRESS, host + ":" + port)
                .setTag(Constants.Tag.TAG_COMPONENT, "dubbo")
                .setTag(TAG_ARGUMENTS, invocation.getArguments() == null ? "" :
                        Arrays.asList(invocation.getArguments()).toString());
        return span;
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Object ret, Span span) throws Throwable {
        Result result = (Result) ret;
        if (result != null && result.getException() != null) {
            TracerManager.error(result.getException(), span);
        } else {
            TracerManager.finish(span);
        }
        return ret;
    }

    @Override
    public void handleMethodException(Method method, Object[] allArguments, Throwable t, Span span) {
        TracerManager.error(t, span);
    }

    private String generateRequestUrl(URL url, Invocation invocation) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(url.getProtocol() + "://");
        requestUrl.append(url.getHost());
        requestUrl.append(":" + url.getPort() + "/");
        requestUrl.append(generateOperationName(url, invocation));
        return requestUrl.toString();
    }

    private String generateOperationName(URL requestUrl, Invocation invocation) {
        StringBuilder operationName = new StringBuilder();
        operationName.append(requestUrl.getPath());
        operationName.append("." + invocation.getMethodName() + "(");
        for (Class<?> classes : invocation.getParameterTypes()) {
            operationName.append(classes.getSimpleName() + ",");
        }
        if (invocation.getParameterTypes().length > 0) {
            operationName.delete(operationName.length() - 1, operationName.length());
        }
        operationName.append(")");
        return operationName.toString();
    }
}

