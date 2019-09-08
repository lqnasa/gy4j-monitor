package cn.gy4j.monitor.collector.service.impl;

import cn.gy4j.monitor.collector.entity.MonitorTracer;
import cn.gy4j.monitor.collector.service.AbstractCollectService;
import cn.gy4j.monitor.collector.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Service
@ConditionalOnProperty(name = "store.type", havingValue = "mysql")
public class CollectServiceMysqlImpl extends AbstractCollectService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveTracer(MonitorTracer monitorTracer) {
        String tags = GsonUtil.objectToJson(monitorTracer.getTags());
        jdbcTemplate.update("INSERT INTO `monitor_tracer`(`id`, `create_time`, `server_name`, `operation_name`, " +
                        "`span_id`, `trace_id`, `parent_id`, `server_instance_id`, `component`, `arguments`, `start_time`, `finish_time`, " +
                        "`cost_time`, `tags`, `error`, `error_message`, `error_stack`, `reference_type`, `hostname`, `ip`, `seq`) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", monitorTracer.getId(), monitorTracer.getCreateTime(), monitorTracer.getServerName(),
                monitorTracer.getOperationName(), monitorTracer.getSpanId(), monitorTracer.getTraceId(), monitorTracer.getParentId(), monitorTracer.getServerInstanceId(),
                monitorTracer.getComponent(), monitorTracer.getArguments(), monitorTracer.getStartTime(), monitorTracer.getFinishTime(), monitorTracer.getCostTime(),
                tags, monitorTracer.isError(), monitorTracer.getErrorMessage(), monitorTracer.getErrorStack(),monitorTracer.getReferenceType(), monitorTracer.getHostname(),
                monitorTracer.getIp(), monitorTracer.getSeq());
    }
}
