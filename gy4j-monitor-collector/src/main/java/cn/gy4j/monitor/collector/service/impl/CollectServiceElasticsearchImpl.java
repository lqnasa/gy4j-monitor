package cn.gy4j.monitor.collector.service.impl;

import cn.gy4j.monitor.collector.entity.JvmInfo;
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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Calendar;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-19
 */
@Service
@Slf4j
public class CollectServiceElasticsearchImpl extends AbstractCollectService {


    @Value("${spring.elasticsearch.indexTracer:}")
    private String indexTracerName;

    @Value("${spring.elasticsearch.indexJvm:}")
    private String indexJvmName;

    @Value("${spring.elasticsearch.type:}")
    private String type;

    @Autowired
    private JestClient jestClient;

    private int curYear;
    private int curMonth;

    @PostConstruct
    private void init() {
        Calendar n = Calendar.getInstance();
        int year = n.get(Calendar.YEAR);
        int month = n.get(Calendar.MONTH) + 1;
        resetIndexName(year, month);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.currentThread().sleep(1000L);
                        Calendar n = Calendar.getInstance();
                        int year = n.get(Calendar.YEAR);
                        int month = n.get(Calendar.MONTH) + 1;
                        if (year != curYear || month != curMonth) {
                            resetIndexName(year, month);
                        }
                    } catch (Exception ex) {
                        log.warn(ex.getMessage(), ex);
                    }
                }
            }
        }).start();
    }

    /**
     * 重置索引名称，分月记录.
     *
     * @param year  年
     * @param month 月
     */
    private void resetIndexName(int year, int month) {
        curYear = year;
        curMonth = month;
        indexTracerName = indexTracerName.split("-")[0] + "-" + year + "." + month;
        indexJvmName = indexJvmName.split("-")[0] + "-" + year + "." + month;
    }

    @Override
    public void saveTracer(MonitorTracer monitorTracer) throws IOException {
        //构建一个索引
        Index index = new Index.Builder(monitorTracer).index(indexTracerName).type(type).build();
        //执行
        JestResult jestResult = jestClient.execute(index);
        if (!StringUtils.isEmpty(jestResult.getErrorMessage())) {
            log.error("写入es异常monitorTracer：" + jestResult.getErrorMessage());
        }
    }

    @Override
    public void saveJvm(JvmInfo jvmInfo) throws Exception {
        //构建一个索引
        Index index = new Index.Builder(jvmInfo).index(indexJvmName).type(type).build();
        //执行
        JestResult jestResult = jestClient.execute(index);
        if (!StringUtils.isEmpty(jestResult.getErrorMessage())) {
            log.error("写入es异常jvmInfo：" + jestResult.getErrorMessage());
        }
    }
}
