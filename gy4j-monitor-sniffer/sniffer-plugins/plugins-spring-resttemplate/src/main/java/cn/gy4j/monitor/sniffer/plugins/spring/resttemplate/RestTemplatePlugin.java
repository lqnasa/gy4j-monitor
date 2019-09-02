package cn.gy4j.monitor.sniffer.plugins.spring.resttemplate;

import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptorTemplate;
import cn.gy4j.monitor.sniffer.core.plugin.api.Plugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class RestTemplatePlugin implements Plugin {
    private static final String ENHANCE_CLASS_NAME = "org.springframework.web.client.RestTemplate";
    private static final String INTERCEPTOR_CLASS_NAME_DO_EXECUTE = "cn.gy4j.monitor.sniffer.plugins.spring.resttemplate.RestTemplateDoExecuteInterceptor";
    private static final String INTERCEPTOR_CLASS_NAME_CREATE_REQUEST = "cn.gy4j.monitor.sniffer.plugins.spring.resttemplate.RestTemplateCreateRequestInterceptor";
    private static final String INTERCEPTOR_CLASS_NAME_HANDLE_RESPONSE = "cn.gy4j.monitor.sniffer.plugins.spring.resttemplate.RestTemplateHandleResponseInterceptor";
    private static final String ENHANCE_METHOD_DO_EXECUTE = "doExecute";
    private static final String ENHANCE_METHOD_CREATE_REQUEST = "createRequest";
    private static final String ENHANCE_METHOD_HANDLE_RESPONSE = "handleResponse";

    @Override
    public ElementMatcher.Junction<TypeDescription> buildJunction() {
        return ElementMatchers.named(ENHANCE_CLASS_NAME);
    }

    @Override
    public DynamicType.Builder<?> enhance(DynamicType.Builder<?> newBuilder, TypeDescription typeDescription, ClassLoader classLoader) {
        newBuilder = newBuilder.method(named(ENHANCE_METHOD_DO_EXECUTE))
                .intercept(MethodDelegation.withDefaultConfiguration().to(InstMethodInterceptorTemplate.getTemplate(INTERCEPTOR_CLASS_NAME_DO_EXECUTE, classLoader)));
        newBuilder = newBuilder.method(named(ENHANCE_METHOD_CREATE_REQUEST))
                .intercept(MethodDelegation.withDefaultConfiguration().to(InstMethodInterceptorTemplate.getTemplate(INTERCEPTOR_CLASS_NAME_CREATE_REQUEST, classLoader)));
        newBuilder = newBuilder.method(named(ENHANCE_METHOD_HANDLE_RESPONSE))
                .intercept(MethodDelegation.withDefaultConfiguration().to(InstMethodInterceptorTemplate.getTemplate(INTERCEPTOR_CLASS_NAME_HANDLE_RESPONSE, classLoader)));
        return newBuilder;
    }
}
