package cn.gy4j.monitor.sniffer.agent;

import cn.gy4j.monitor.sniffer.core.config.AgentConfigInitializer;
import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.plugin.PluginsManager;
import cn.gy4j.monitor.sniffer.core.remote.RemoteManager;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

/**
 * javaagent代理入口.
 * <p>
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class SnifferAgent {
    private static final ILogger logger = LoggerFactory.getLogger(SnifferAgent.class);

    /**
     * 在方法在main方法之前执行，和main方法同Jvm、ClassLoader、Security policy和Context.
     *
     * @param agentOps javaagent入参
     * @param inst     对class进行字节码加强的代理实例
     */
    public static void premain(String agentOps, Instrumentation inst) {
        logger.info("加载：gy4j-monitor-sniffer-agent");

        // 初始化Agent的配置
        AgentConfigInitializer.init();
        // 插件初始化
        PluginsManager.init();
        // 初始化采集
        RemoteManager.init();

        // 基于ByteBuddy建立agent规则
        final ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.ENABLED);
        new AgentBuilder.Default(byteBuddy)
                .ignore(initIgnoreElementMatcher()) // 忽略规则
                .type(initElementMatcher()) // 匹配规则
                .transform(new AgentTransformer()) // 转换规则
                .with(new AgentListener()) // 侦听器
                .installOn(inst);
    }

    private static ElementMatcher<? super TypeDescription> initElementMatcher() {
        return PluginsManager.buildMatch();
    }

    /**
     * 忽略规则构建.
     *
     * @return
     */
    private static ElementMatcher<TypeDescription> initIgnoreElementMatcher() {
        // synthetic总的来说，是由编译器引入的字段、方法、类或其他结构，主要用于JVM内部使用
        // 参考：https://blog.csdn.net/a327369238/article/details/52608805
        return nameStartsWith("net.bytebuddy.").or(ElementMatchers.<TypeDescription>isSynthetic());
    }

    /**
     * 转换规则构建.
     */
    static class AgentTransformer implements AgentBuilder.Transformer {
        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            return PluginsManager.enhanceBuilder(builder, typeDescription, classLoader);
        }
    }

    /**
     * 侦听器.
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
