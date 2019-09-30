package cn.gy4j.monitor.collector.config;

import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-19
 */
@Configuration
public class ElasticsearchConfig {
    @Value("#{'${spring.elasticsearch.jest.uris}'.split(',')}")
    private List<String> uris;

    /**
     * javabean：jestClientc初始化.
     *
     * @return
     */
    @Bean
    public JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(uris)
                .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create())
                .multiThreaded(true)
                .readTimeout(10000)
                .build());
        JestClient client = factory.getObject();
        return client;
    }
}
