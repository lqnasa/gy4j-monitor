package cn.gy4j.monitor.sniffer.core.plugin.api;

import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-27
 */
public class InstMethodInterceptorTemplate {
    private static final ILogger logger = LoggerFactory.getLogger(InstMethodInterceptorTemplate.class);

    /**
     * 实例方法拦截实现对象.
     */
    private InstMethodInterceptor interceptor;


    /**
     * 获取实例方法拦截模板对象.
     *
     * @param instMethodInterceptor 拦截器
     * @return
     */
    public static InstMethodInterceptorTemplate getTemplate(InstMethodInterceptor instMethodInterceptor) {
        InstMethodInterceptorTemplate template = new InstMethodInterceptorTemplate();
        template.interceptor = instMethodInterceptor;
        return template;
    }

    /**
     * 基于ByteBuddy的拦截方法.
     *
     * @param obj          原对象
     * @param allArguments 参数
     * @param zuper        原调用
     * @param method       方法
     * @return
     */
    @RuntimeType
    public Object intercept(@This Object obj,
                            @AllArguments Object[] allArguments,
                            @SuperCall Callable<?> zuper,
                            @Origin Method method) throws Throwable {
        try {
            interceptor.beforeMethod(method, allArguments);
        } catch (Throwable t) {
            logger.error(t, "class[" + obj.getClass() + "] before method[" + method.getName() + "] intercept failure");
        }

        Object ret = null;

        try {
            ret = zuper.call();
        } catch (Throwable t) {
            try {
                interceptor.handleMethodException(method, allArguments, t);
            } catch (Throwable t2) {
                logger.error(t2, "class[" + obj.getClass() + "] handle method[" + method.getName() + "] exception failure");
            }
            throw t;
        } finally {
            try {
                ret = interceptor.afterMethod(method, allArguments, ret);
            } catch (Throwable t) {
                logger.error(t, "class[" + obj.getClass() + "] after method[" + method.getName() + "] intercept failure");
            }
        }
        return ret;
    }
}
