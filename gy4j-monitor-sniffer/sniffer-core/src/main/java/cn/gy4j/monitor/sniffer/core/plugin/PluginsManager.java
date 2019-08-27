package cn.gy4j.monitor.sniffer.core.plugin;

import cn.gy4j.monitor.sniffer.core.plugin.api.Plugin;
import cn.gy4j.monitor.sniffer.plugins.test.bytebuddy.TestByteBuddyPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.LinkedList;
import java.util.List;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-27
 */
public class PluginsManager {
    /**
     * 插件列表.
     */
    private static List<Plugin> plugins = new LinkedList<>();

    /**
     * 插件初始化.
     */
    public static void init() {
        plugins.add(new TestByteBuddyPlugin());
    }

    /**
     * 插件匹配规则构建.
     *
     * @return
     */
    public static ElementMatcher<TypeDescription> buildMatch() {
        ElementMatcher.Junction junction = ElementMatchers.none();
        for (Plugin plugin : plugins) {
            junction = junction.or(plugin.buildJunction());
        }
        return junction;
    }

    /**
     * 根据类型匹配的插件对builder进行加强，实现对类的加强.
     *
     * @param builder         原构建器
     * @param typeDescription 类型对象
     * @param classLoader     类加载器
     * @return
     */
    public static DynamicType.Builder<?> enhanceBuilder(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
        List<Plugin> matchedPlugins = findPlugins(typeDescription);
        DynamicType.Builder<?> newBuilder = builder;
        for (Plugin plugin : matchedPlugins) {
            newBuilder = plugin.enhance(newBuilder, typeDescription, classLoader);
        }
        return newBuilder;
    }

    /**
     * 根据typeDescription获取插件列表.
     *
     * @param typeDescription 类型对象
     * @return
     */
    private static List<Plugin> findPlugins(TypeDescription typeDescription) {
        List<Plugin> matchedPlugins = new LinkedList<>();
        for (Plugin plugin : plugins) {
            if (plugin.buildJunction().matches(typeDescription)) {
                matchedPlugins.add(plugin);
            }
        }
        return matchedPlugins;
    }
}
