package cn.gy4j.monitor.test.plugins.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class Provider {
    /**
     * 生产者测试入口.
     * -javaagent:sniffer-agent.jar -DSA_LOG_TYPE=STDOUT -DSA_LOG_LEVEL=DEBUG
     *
     * @param args 入参
     */
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");
        context.start();
        System.in.read(); // 按任意键退出
    }
}
