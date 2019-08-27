package cn.gy4j.monitor.sniffer.core.plugin;

import cn.gy4j.monitor.sniffer.core.util.StringUtil;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class PluginDefine {
    /**
     * 插件名称.
     */
    private String pluginName;
    /**
     * 插件类名.
     */
    private String pluginClassName;

    /**
     * 构造函数.
     *
     * @param pluginName        插件名
     * @param pluginClassName   插件类名
     */
    public PluginDefine(String pluginName, String pluginClassName) {
        this.pluginName = pluginName;
        this.pluginClassName = pluginClassName;
    }

    /**
     * 根据配置构建插件定义对象.
     *
     * @param define 插件配置字符串（格式：pluginName=pluginClassName）
     * @return
     */
    public static PluginDefine build(String define) throws IllegalPluginDefineException {
        if (StringUtil.isEmpty(define)) {
            throw new IllegalPluginDefineException(define);
        }
        String[] pluginDefine = define.split("=");
        if (pluginDefine.length != 2) {
            throw new IllegalPluginDefineException(define);
        }
        String pluginName = pluginDefine[0];
        String pluginClassName = pluginDefine[1];
        return new PluginDefine(pluginName, pluginClassName);
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginClassName() {
        return pluginClassName;
    }
}
