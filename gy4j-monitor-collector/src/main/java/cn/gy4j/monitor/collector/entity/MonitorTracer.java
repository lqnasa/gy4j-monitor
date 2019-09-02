package cn.gy4j.monitor.collector.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Data
public class MonitorTracer {
    /**
     * 主键ID.
     */
    @Id
    private String id;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 服务名.
     */
    private String serverName;

    /**
     * 操作名.
     */
    private String operationName;

    /**
     * spanId.
     */
    private String spanId;

    /**
     * traceId.
     */
    private String traceId;

    /**
     * 父的spanId.
     */
    private String parentId;

    /**
     * 服务实例ID.
     */
    private String serverInstanceId;

    /**
     * 组件.
     */
    private String component;

    /**
     * 参数.
     */
    private String arguments;

    /**
     * 执行开始时间.
     */
    private Date startTime;

    /**
     * 执行结束时间.
     */
    private Date finishTime;

    /**
     * 执行时长：毫秒.
     */
    private Integer costTime;

    /**
     * 标签json串.
     */
    private Map<String, Object> tags;

    /**
     * true 成功，false 失败.
     */
    private boolean error;

    /**
     * 异常信息.
     */
    private String errorMessage;

    /**
     * 异常堆栈信息.
     */
    private String errorStack;

    /**
     * 依赖类型：0 父子，1 跟随.
     */
    private String referenceType;

    /**
     * 主机名.
     */
    private String hostname;

    /**
     * ip.
     */
    private String ip;

    /**
     * 排序.
     */
    private Integer seq;
}
