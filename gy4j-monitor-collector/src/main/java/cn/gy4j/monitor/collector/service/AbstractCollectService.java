package cn.gy4j.monitor.collector.service;

import cn.gy4j.monitor.collector.bean.TransportSpan;
import cn.gy4j.monitor.collector.bean.TransportTracer;
import cn.gy4j.monitor.collector.constant.Constants;
import cn.gy4j.monitor.collector.entity.MonitorTracer;
import cn.gy4j.monitor.collector.util.DateUtil;
import cn.gy4j.monitor.collector.util.GsonUtil;
import cn.gy4j.monitor.collector.util.IdUtil;
import cn.gy4j.monitor.collector.util.IntegerUtil;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public abstract class AbstractCollectService implements CollectService {

    @Override
    public void saveTracer(TransportTracer transportTracer) throws Exception {
        for (int i = 0; i < transportTracer.getSpans().size(); i++) {
            MonitorTracer monitorTracer = genMonitorTracer(transportTracer, transportTracer.getSpans().get(i));
            if (monitorTracer == null) {
                return;
            }
            saveTracer(monitorTracer);
        }
    }

    public abstract void saveTracer(MonitorTracer monitorTracer) throws IOException;

    private MonitorTracer genMonitorTracer(TransportTracer transportTracer, TransportSpan transportSpan) {
        MonitorTracer monitorTracer = new MonitorTracer();
        monitorTracer.setId(IdUtil.getId());
        monitorTracer.setCreateTime(new Date());
        monitorTracer.setServerName(transportTracer.getServerName());
        monitorTracer.setServerInstanceId(transportTracer.getServerInstanceId());
        monitorTracer.setHostname(transportTracer.getHostname());
        monitorTracer.setIp(transportTracer.getIp());
        monitorTracer.setSpanId(transportSpan.getSpanId());
        monitorTracer.setTraceId(transportSpan.getTraceId());
        monitorTracer.setParentId(transportSpan.getParentId());
        monitorTracer.setOperationName(transportSpan.getOperationName());
        monitorTracer.setStartTime(DateUtil.milliSecondToDate(transportSpan.getStartTime()));
        monitorTracer.setFinishTime(DateUtil.milliSecondToDate(transportSpan.getFinishTime()));
        monitorTracer.setCostTime((int) (transportSpan.getFinishTime() - transportSpan.getStartTime()));
        monitorTracer.setReferenceType(transportSpan.getReferenceType());

        Map<String, Object> tagMap = GsonUtil.jsonToMap(transportSpan.getTags());
        if (tagMap.containsKey(Constants.Tag.TAG_ERROR)) {
            boolean isError = (boolean) tagMap.get(Constants.Tag.TAG_ERROR);
            monitorTracer.setError(isError);
            tagMap.remove(Constants.Tag.TAG_ERROR);
        }
        if (tagMap.containsKey(Constants.Tag.TAG_COMPONENT)) {
            monitorTracer.setComponent(String.valueOf(tagMap.get(Constants.Tag.TAG_COMPONENT)));
            tagMap.remove(Constants.Tag.TAG_COMPONENT);
        }
        if (tagMap.containsKey(Constants.Tag.TAG_ERROR_MESSAGE)) {
            monitorTracer.setErrorMessage(String.valueOf(tagMap.get(Constants.Tag.TAG_ERROR_MESSAGE)));
            tagMap.remove(Constants.Tag.TAG_ERROR_MESSAGE);
        }
        if (tagMap.containsKey(Constants.Tag.TAG_ERROR_STACK)) {
            monitorTracer.setErrorStack(String.valueOf(tagMap.get(Constants.Tag.TAG_ERROR_STACK)));
            tagMap.remove(Constants.Tag.TAG_ERROR_STACK);
        }
        if (tagMap.containsKey(Constants.Tag.TAG_ARGUMENTS)) {
            monitorTracer.setArguments(String.valueOf(tagMap.get(Constants.Tag.TAG_ARGUMENTS)));
            tagMap.remove(Constants.Tag.TAG_ARGUMENTS);
        }
        if (tagMap.containsKey(Constants.Tag.TAG_ORDER)) {
            monitorTracer.setSeq(tagMap.get(Constants.Tag.TAG_ORDER) == null ? 0
                    : IntegerUtil.valueOf(tagMap.get(Constants.Tag.TAG_ORDER)));
            tagMap.remove(Constants.Tag.TAG_ORDER);
        }
        monitorTracer.setTags(tagMap);
        return monitorTracer;
    }
}
