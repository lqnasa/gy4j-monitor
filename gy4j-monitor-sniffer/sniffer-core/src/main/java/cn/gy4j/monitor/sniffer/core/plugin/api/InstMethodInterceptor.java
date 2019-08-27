package cn.gy4j.monitor.sniffer.core.plugin.api;

import java.lang.reflect.Method;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-27
 */
public interface InstMethodInterceptor {
    /**
     * 方法执行前.
     *
     * @param method       方法对象
     * @param allArguments 参数
     */
    void beforeMethod(Method method, Object[] allArguments) throws Throwable;

    /**
     * 方法执行后.
     *
     * @param method       方法对象
     * @param allArguments 参数
     * @param ret          返回对象
     * @return
     */
    Object afterMethod(Method method, Object[] allArguments, Object ret) throws Throwable;

    /**
     * 方法执行异常.
     *
     * @param method       方法对象
     * @param allArguments 参数
     * @param throwable    异常对象
     */
    void handleMethodException(Method method, Object[] allArguments, Throwable throwable);
}
