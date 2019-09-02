package cn.gy4j.monitor.collector.config;

import cn.gy4j.monitor.collector.config.prop.DatasourceProperties;
import cn.gy4j.monitor.collector.util.GsonUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "store.type", havingValue = "mysql")
@EnableConfigurationProperties({DatasourceProperties.class})
public class MySqlConfig {
    @PostConstruct
    public void postConstruct() {
        log.info("加载配置：MySqlConfig");
    }

    @Autowired
    private DatasourceProperties datasourceProperties;

    /**
     * javabean：druid数据源.
     *
     * @return
     */
    @Bean
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        try {
            BeanUtils.copyProperties(datasourceProperties, datasource);
        } catch (Exception e) {
            log.error("druid configuration initialization filter", e);
        }
        datasource.setConnectProperties(datasourceProperties.getConnectionProperties());
        initDb(datasource);
        return datasource;
    }

    /**
     * 初始化数据库.
     *
     * @param datasource 数据源
     */
    private void initDb(DataSource datasource) {
        BufferedReader bufferedReader = null;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
            // 需要初始化
            log.info("开始初始化数据库表");
            try {
                // 通过异常检测是否需要初始化数据库
                jdbcTemplate.queryForList("select 1 from monitor_tracer limit 1");
                log.info("数据库已经存在，不需要初始化");
            } catch (Exception ex) {
                InputStream bi = (InputStream) Resources.getResource("init.sql").getContent();
                StringBuffer sb = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(bi));
                String str = bufferedReader.readLine();
                while (str != null) {
                    sb.append(str);
                    str = bufferedReader.readLine();
                }
                String[] sqls = sb.toString().split(";");
                log.info("初始化：" + GsonUtil.objectToJson(sqls));
                jdbcTemplate.batchUpdate(sqls);
                log.info("初始化数据库表成功");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            log.info("初始化数据库表失败");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }
}
