package cn.gy4j.monitor.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }
}