package cn.gy4j.monitor.sniffer.plugins.test.bytebuddy;

import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.plugin.api.InstMethodInterceptor;

import java.lang.reflect.Method;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class TestByteBuddyInterceptor implements InstMethodInterceptor {
    private static final ILogger logger = LoggerFactory.getLogger(TestByteBuddyInterceptor.class);

    @Override
    public void beforeMethod(Method method, Object[] allArguments) {
        // 原方法执行前
        logger.info("before method:" + method.getName());
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Object ret) {
        logger.info("after method:" + method.getName());
        return ret;
    }

    @Override
    public void handleMethodException(Method method, Object[] allArguments, Throwable throwable) {
        // 原方法调用异常
        logger.info("exception method:" + method.getName());
    }
}
