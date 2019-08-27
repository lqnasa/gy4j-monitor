package cn.gy4j.monitor.sniffer.core.plugin.api;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-27
 */
public interface Plugin {
    /**
     * 抽象方法：获取className.
     *
     * @return 插件的匹配器
     */
    ElementMatcher.Junction<TypeDescription> buildJunction();

    /**
     * builder转换规则聚合.
     *
     * @param newBuilder      原加强构建器
     * @param typeDescription 类型信息
     * @param classLoader     类加载器
     * @return
     */
    DynamicType.Builder<?> enhance(DynamicType.Builder<?> newBuilder, TypeDescription typeDescription, ClassLoader classLoader);
}
