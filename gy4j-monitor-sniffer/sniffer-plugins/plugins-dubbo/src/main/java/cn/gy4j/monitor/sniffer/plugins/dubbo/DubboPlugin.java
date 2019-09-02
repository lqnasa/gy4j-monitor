package cn.gy4j.monitor.sniffer.plugins.dubbo;

import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptorTemplate;
import cn.gy4j.monitor.sniffer.core.plugin.api.Plugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class DubboPlugin implements Plugin {
    private static final String ENHANCE_CLASS_ANNOTATION = "com.alibaba.dubbo.monitor.support.MonitorFilter";
    private static final String ENHANCE_METHOD_NAME = "invoke";
    private static final String INTERCEPTOR_CLASS_NAME = "cn.gy4j.monitor.sniffer.plugins.dubbo.DubboInterceptor";

    @Override
    public ElementMatcher.Junction<TypeDescription> buildJunction() {
        return named(ENHANCE_CLASS_ANNOTATION);
    }

    @Override
    public DynamicType.Builder<?> enhance(DynamicType.Builder<?> newBuilder, TypeDescription typeDescription, ClassLoader classLoader) {
        return newBuilder.method(named(ENHANCE_METHOD_NAME)).intercept(MethodDelegation.withDefaultConfiguration()
                .to(InstMethodInterceptorTemplate.getTemplate(INTERCEPTOR_CLASS_NAME, classLoader)));
    }
}
