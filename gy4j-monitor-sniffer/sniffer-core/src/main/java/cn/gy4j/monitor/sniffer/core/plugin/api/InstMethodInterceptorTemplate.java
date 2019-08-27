package cn.gy4j.monitor.sniffer.core.plugin.api;

import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.plugin.loader.InterceptorInstanceLoader;
import net.bytebuddy.description.type.TypeDescription;
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
     * @param instanceMethodsAroundInterceptorClassName 拦截器的类全名
     * @param classLoader                               类加载器
     * @return
     */
    public static InstMethodInterceptorTemplate getTemplate(String instanceMethodsAroundInterceptorClassName, ClassLoader classLoader) {
        return new InstMethodInterceptorTemplate(instanceMethodsAroundInterceptorClassName, classLoader);
    }

    private InstMethodInterceptorTemplate(String instanceMethodsAroundInterceptorClassName, ClassLoader classLoader) {
        try {
            interceptor = InterceptorInstanceLoader.load(instanceMethodsAroundInterceptorClassName, classLoader);
        } catch (Throwable t) {
            throw new RuntimeException("Can't create InstanceMethodsAroundInterceptor.", t);
        }
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
