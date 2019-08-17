package cn.gy4j.monitor.sniffer.agent;

import cn.gy4j.monitor.sniffer.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.logging.api.ILogger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

/**
 * javaagent代理入口
 * <p>
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class SnifferAgent {
    private static final ILogger logger = LoggerFactory.getLogger(TestByteBuddyPrintInterceptor.class);

    /**
     * 在方法在main方法之前执行，和main方法同Jvm、ClassLoader、Security policy和Context
     *
     * @param agentOps javaagent入参
     * @param inst     对class进行字节码加强的代理实例
     */
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("hello javaagent!this is premain!");
        // 基于ByteBuddy建立agent规则
        final ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.ENABLED);
        new AgentBuilder.Default(byteBuddy)
                .ignore(initIgnoreElementMatcher()) // 忽略规则
                .type(ElementMatchers.<TypeDescription>named("cn.gy4j.monitor.test.sniffer.agent.TestByteBuddy")) // 匹配规则
                .transform(new AgentTransformer()) // 转换规则
                .with(new AgentListener()) // 侦听器
                .installOn(inst);
    }

    /**
     * 忽略规则构建
     *
     * @return
     */
    private static ElementMatcher<TypeDescription> initIgnoreElementMatcher() {
        // synthetic总的来说，是由编译器引入的字段、方法、类或其他结构，主要用于JVM内部使用
        // 参考：https://blog.csdn.net/a327369238/article/details/52608805
        return nameStartsWith("net.bytebuddy.").or(ElementMatchers.<TypeDescription>isSynthetic());
    }

    /**
     * 转换规则构建
     */
    static class AgentTransformer implements AgentBuilder.Transformer {
        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            // 根据插件聚合转换规则
            return builder.method(ElementMatchers.<MethodDescription>named("print"))
                    .intercept(MethodDelegation.withDefaultConfiguration().to(new TestByteBuddyPrintInterceptor()));
        }
    }

    /**
     * 侦听器
     */
    static class AgentListener implements AgentBuilder.Listener {

        @Override
        public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            logger.info("onTransformation:" + typeDescription);
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            logger.error(throwable, "onError:" + typeName);
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        }
    }
}
