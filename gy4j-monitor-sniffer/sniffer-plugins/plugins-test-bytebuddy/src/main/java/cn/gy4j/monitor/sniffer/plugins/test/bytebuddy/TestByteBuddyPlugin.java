package cn.gy4j.monitor.sniffer.plugins.test.bytebuddy;

import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptorTemplate;
import cn.gy4j.monitor.sniffer.core.plugin.api.Plugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;


/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-27
 */
public class TestByteBuddyPlugin implements Plugin {
    private static final String ENHANCE_CLASS_NAME = "cn.gy4j.monitor.test.plugins.test.bytebuddy.TestByteBuddy";
    private static final String INTERCEPTOR_CLASS_NAME_PRINT = "cn.gy4j.monitor.sniffer.plugins.test.bytebuddy.TestByteBuddyInterceptor";

    @Override
    public ElementMatcher.Junction<TypeDescription> buildJunction() {
        return ElementMatchers.<TypeDescription>named(ENHANCE_CLASS_NAME);
    }

    @Override
    public DynamicType.Builder<?> enhance(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
        return builder.method(ElementMatchers.<MethodDescription>named("print"))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .to(InstMethodInterceptorTemplate.getTemplate(INTERCEPTOR_CLASS_NAME_PRINT,classLoader)));
    }
}
