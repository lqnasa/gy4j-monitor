package cn.gy4j.monitor.sniffer.agent;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class TestByteBuddyPrintInterceptor {
    @RuntimeType
    public Object intercept(@This Object obj,
                            @AllArguments Object[] allArguments,
                            @SuperCall Callable<?> zuper,
                            @Origin Method method) throws Throwable {
        try {
            System.out.println("before method:" + method.getName());
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("class[" + obj.getClass() + "] before method[" + method.getName() + "] intercept failure");
        }

        Object ret = null;

        try {
            ret = zuper.call();
        } catch (Throwable t) {
            try {
                System.out.println("error method:" + method.getName());
            } catch (Throwable t2) {
                System.out.println("class[" + obj.getClass() + "] handle method[" + method.getName() + "] exception failure");
            }
            throw t;
        } finally {
            try {
                System.out.println("after method:" + method.getName());
            } catch (Throwable t) {
                System.out.println("class[" + obj.getClass() + "] after method[" + method.getName() + "] intercept failure");
            }
        }
        return ret;
    }
}
