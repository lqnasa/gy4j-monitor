package cn.gy4j.monitor.sniffer.core.transport;


import cn.gy4j.monitor.sniffer.core.remote.RemoteEvent;
import cn.gy4j.monitor.sniffer.core.util.GsonUtil;

import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JVM信息传输类.
 *
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-09-30
 */
public class TransportJvm implements Transportor {
    private String serverInstanceId;
    private String serverName;
    private String hostname;
    private String ip;
    private Date createTime = new Date();

    private int threadCount;
    private long threadCpuTime;
    private long threadUserTime;

    private long loadedClassCount;
    private long totalLoadedClassCount;
    private long unloadedClassCount;

    private int availableProcessors;
    private double systemLoadAverage;

    private Map<String, MemoryUsage> heapMemoryInfo = new HashMap<>();

    private MemoryUsage heapMemoryUsage;
    private MemoryUsage nonHeapMemoryUsage;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServerInstanceId() {
        return serverInstanceId;
    }

    public void setServerInstanceId(String serverInstanceId) {
        this.serverInstanceId = serverInstanceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getThreadCpuTime() {
        return threadCpuTime;
    }

    public void setThreadCpuTime(long threadCpuTime) {
        this.threadCpuTime = threadCpuTime;
    }

    public long getThreadUserTime() {
        return threadUserTime;
    }

    public void setThreadUserTime(long threadUserTime) {
        this.threadUserTime = threadUserTime;
    }

    public long getLoadedClassCount() {
        return loadedClassCount;
    }

    public void setLoadedClassCount(long loadedClassCount) {
        this.loadedClassCount = loadedClassCount;
    }

    public long getTotalLoadedClassCount() {
        return totalLoadedClassCount;
    }

    public void setTotalLoadedClassCount(long totalLoadedClassCount) {
        this.totalLoadedClassCount = totalLoadedClassCount;
    }

    public long getUnloadedClassCount() {
        return unloadedClassCount;
    }

    public void setUnloadedClassCount(long unloadedClassCount) {
        this.unloadedClassCount = unloadedClassCount;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public MemoryUsage getHeapMemoryUsage() {
        return heapMemoryUsage;
    }

    public void setHeapMemoryUsage(MemoryUsage heapMemoryUsage) {
        this.heapMemoryUsage = heapMemoryUsage;
    }

    public MemoryUsage getNonHeapMemoryUsage() {
        return nonHeapMemoryUsage;
    }

    public void setNonHeapMemoryUsage(MemoryUsage nonHeapMemoryUsage) {
        this.nonHeapMemoryUsage = nonHeapMemoryUsage;
    }

    public Map<String, MemoryUsage> getHeapMemoryInfo() {
        return heapMemoryInfo;
    }

    public void setHeapMemoryInfo(Map<String, MemoryUsage> heapMemoryInfo) {
        this.heapMemoryInfo = heapMemoryInfo;
    }

    @Override
    public RemoteEvent getRemoteEvent() {
        return RemoteEvent.JVM;
    }

    @Override
    public String getContent() {
        return GsonUtil.objectToJson(this);
    }
}
