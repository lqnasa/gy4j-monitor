package cn.gy4j.monitor.collector.entity;


import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-09-30
 */
@Data
public class JvmInfo {
    private String serverInstanceId;
    private String serverName;
    private String hostname;
    private String ip;
    private Date createTime;

    private int threadCount;
    private long threadCpuTime;
    private long threadUserTime;

    private long loadedClassCount;
    private long totalLoadedClassCount;
    private long unloadedClassCount;

    private int availableProcessors;
    private double systemLoadAverage;

    private Map<String, MemoryUsage> heapMemoryInfo;

    private MemoryUsage heapMemoryUsage;
    private MemoryUsage nonHeapMemoryUsage;

    public static class MemoryUsage {
        private long init;
        private long used;
        private long committed;
        private long max;

        public long getInit() {
            return init;
        }

        public void setInit(long init) {
            this.init = init;
        }

        public long getUsed() {
            return used;
        }

        public void setUsed(long used) {
            this.used = used;
        }

        public long getCommitted() {
            return committed;
        }

        public void setCommitted(long committed) {
            this.committed = committed;
        }

        public long getMax() {
            return max;
        }

        public void setMax(long max) {
            this.max = max;
        }
    }
}
