package cn.gy4j.monitor.sniffer.agent;

import cn.gy4j.monitor.sniffer.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.logging.api.ILogger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class TestByteBuddyPrintInterceptor {
    private static final ILogger logger = LoggerFactory.getLogger(TestByteBuddyPrintInterceptor.class);

    @RuntimeType
    public Object intercept(@This Object obj,
                            @AllArguments Object[] allArguments,
                            @SuperCall Callable<?> zuper,
                            @Origin Method method) throws Throwable {
        try {
            logger.info("before method:" + method.getName());
        } catch (Throwable t) {
            logger.error(t, "class[" + obj.getClass() + "] before method[" + method.getName() + "] intercept failure");
        }

        Object ret = null;

        try {
            ret = zuper.call();
        } catch (Throwable t) {
            try {
                logger.info("error method:" + method.getName());
            } catch (Throwable t2) {
                logger.error(t2, "class[" + obj.getClass() + "] handle method[" + method.getName() + "] exception failure");
            }
            throw t;
        } finally {
            try {
                logger.info("after method:" + method.getName());
            } catch (Throwable t) {
                logger.error(t, "class[" + obj.getClass() + "] after method[" + method.getName() + "] intercept failure");
            }
        }
        return ret;
    }
}
