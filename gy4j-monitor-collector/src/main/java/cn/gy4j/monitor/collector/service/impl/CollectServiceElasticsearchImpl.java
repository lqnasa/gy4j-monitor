package cn.gy4j.monitor.collector.service.impl;

import cn.gy4j.monitor.collector.entity.MonitorTracer;
import cn.gy4j.monitor.collector.service.AbstractCollectService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-19
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "store.type", havingValue = "es")
public class CollectServiceElasticsearchImpl extends AbstractCollectService {


    @Value("${spring.elasticsearch.index:}")
    private String indexName;

    @Value("${spring.elasticsearch.type:}")
    private String type;

    @Autowired
    private JestClient jestClient;

    @Override
    public void saveTracer(MonitorTracer monitorTracer) throws IOException {
        //构建一个索引
        Index index = new Index.Builder(monitorTracer).index(indexName).type(type).build();
        //执行
        JestResult jestResult = jestClient.execute(index);
        if (!StringUtils.isEmpty(jestResult.getErrorMessage())) {
            log.error("写入es异常：" + jestResult.getErrorMessage());
        }
    }
}
