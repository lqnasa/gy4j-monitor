package cn.gy4j.monitor.sniffer.core.util;

import cn.gy4j.monitor.sniffer.core.config.AgentConfig;

import java.util.List;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class SpanUtil {

    /**
     * 判断是否是需要过滤的操作.
     *
     * @param operationName 操作名称
     * @return
     */
    public static boolean isIgnoreOperationName(String operationName) {
        List<String> ignoreOperations = AgentConfig.Agent.IGNORE_OPERATION;
        if (ignoreOperations != null && ignoreOperations.size() > 0) {
            for (int i = 0; i < ignoreOperations.size(); i++) {
                if (isIgnoreOperationName(ignoreOperations.get(i), operationName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断操作是否需要过滤.
     *
     * @param ignore        过滤规则
     * @param operationName 操作名称
     * @return
     */
    private static boolean isIgnoreOperationName(String ignore, String operationName) {
        if (ignore.startsWith("%") && ignore.endsWith("%")) {
            ignore = ignore.replace("%", "");
            return operationName.indexOf(ignore) != -1;
        }
        if (ignore.startsWith("%") && !ignore.endsWith("%")) {
            ignore = ignore.replace("%", "");
            return operationName.endsWith(ignore);
        }
        if (!ignore.startsWith("%") && ignore.endsWith("%")) {
            ignore = ignore.replace("%", "");
            return operationName.startsWith(ignore);
        }
        return ignore.equals(operationName);
    }
}
