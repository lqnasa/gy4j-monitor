package cn.gy4j.monitor.test.plugins.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class Consummer {
    private static final Logger logger = Logger.getLogger(Consummer.class.getName());

    /**
     * 消费者测试入口.
     * -javaagent:sniffer-agent.jar -DSA_LOG_TYPE=STDOUT -DSA_LOG_LEVEL=DEBUG
     *
     * @param args 入参
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法
        logger.info(hello); // 显示调用结果
    }
}
