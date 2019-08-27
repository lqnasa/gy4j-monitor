package cn.gy4j.monitor.sniffer.core.plugin;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class IllegalPluginDefineException extends Exception {
    public IllegalPluginDefineException(String define) {
        super("Illegal plugin define : " + define);
    }
}