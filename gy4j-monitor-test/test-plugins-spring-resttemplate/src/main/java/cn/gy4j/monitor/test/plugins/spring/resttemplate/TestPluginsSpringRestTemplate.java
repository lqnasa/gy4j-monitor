package cn.gy4j.monitor.test.plugins.spring.resttemplate;

import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class TestPluginsSpringRestTemplate {
    private static final Logger logger = Logger.getLogger(TestPluginsSpringRestTemplate.class.getName());

    /**
     * -javaagent:sniffer-agent.jar
     *
     * @param args main方法入参
     */
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("https://www.baidu.com/s", String.class);
        logger.info(result);
    }
}
