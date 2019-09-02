package cn.gy4j.monitor.test.plugins.spring.mvc.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
@SpringBootApplication
public class PluginsSpringMvcAnnotationMain {
    /**
     * -javaagent:sniffer-agent.jar.
     *
     * @param args main方法入参
     */
    public static void main(String[] args) {
        SpringApplication.run(PluginsSpringMvcAnnotationMain.class, args);
    }
}
