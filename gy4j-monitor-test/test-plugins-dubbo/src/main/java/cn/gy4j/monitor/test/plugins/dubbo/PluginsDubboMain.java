package cn.gy4j.monitor.test.plugins.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@SpringBootApplication
@ImportResource("classpath:consumer.xml")
public class PluginsDubboMain {
    /**
     * -javaagent:sniffer-agent.jar.
     *
     * @param args main方法入参
     */
    public static void main(String[] args) {
        SpringApplication.run(PluginsDubboMain.class, args);
    }
}
