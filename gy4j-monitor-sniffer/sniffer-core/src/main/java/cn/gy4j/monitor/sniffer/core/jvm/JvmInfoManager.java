package cn.gy4j.monitor.sniffer.core.jvm;



import cn.gy4j.monitor.sniffer.core.config.AgentConfig;
import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.transport.TransportJvm;
import cn.gy4j.monitor.sniffer.core.transport.TransportManager;

import java.lang.management.*;
import java.util.List;

import static cn.gy4j.monitor.sniffer.core.config.AgentConfig.Agent.JVM_COLLECT_INTEVAL;


/**
 * JVM信息收集管理.
 *
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-09-30
 */
public class JvmInfoManager {
    private static final ILogger logger = LoggerFactory.getLogger(JvmInfoManager.class);

    public static void init() {
        new JvmCollectThread().start();
    }

    static class JvmCollectThread extends Thread {
        @Override
        public void run() {
            int sleepTime = JVM_COLLECT_INTEVAL * 1000;
            while (true) {
                try {
                    Thread.currentThread().sleep(sleepTime);
                    TransportJvm transportJvm = getTransportJvm();
                    TransportManager.getInstance().transport(transportJvm);
                } catch (Exception ex) {
                    logger.warn(ex.getMessage(), ex);
                }
            }
        }
    }

    private static TransportJvm getTransportJvm() {
        TransportJvm transportJvm = new TransportJvm();
        transportJvm.setServerInstanceId(AgentConfig.Agent.SERVER_INSTANCE_ID);
        transportJvm.setServerName(AgentConfig.Agent.SERVER_NAME);
        transportJvm.setIp(AgentConfig.Agent.IP);
        transportJvm.setHostname(AgentConfig.Agent.HOSTNAME);
        //================jvm线程信息================
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        //获取当前JVM内的线程数量，该指标非常重要。
        transportJvm.setThreadCount(threadBean.getThreadCount());
        transportJvm.setThreadCpuTime(threadBean.getCurrentThreadCpuTime());
        transportJvm.setThreadUserTime(threadBean.getCurrentThreadUserTime());
        ClassLoadingMXBean classLoadingBean = ManagementFactory.getClassLoadingMXBean();
        //================jvm类加载信息================
        //获取当前JVM加载的类数量
        transportJvm.setLoadedClassCount(classLoadingBean.getLoadedClassCount());
        //获取JVM总加载的类数量
        transportJvm.setTotalLoadedClassCount(classLoadingBean.getTotalLoadedClassCount());
        //获取JVM卸载的类数量
        transportJvm.setUnloadedClassCount(classLoadingBean.getUnloadedClassCount());
        //================jvm的cpu信息================
        OperatingSystemMXBean operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();
        //获取服务器的CPU个数
        transportJvm.setAvailableProcessors(operatingSystemBean.getAvailableProcessors());
        //获取服务器的平均负载。这个指标非常重要，它可以有效的说明当前机器的性能是否正常，如果load过高，说明CPU无法及时处理任务。
        transportJvm.setSystemLoadAverage(operatingSystemBean.getSystemLoadAverage());
        //================jvm的堆内存状态================
        //这里会返回老年代，新生代等内存区的使用情况，按需自取就好
        List<MemoryPoolMXBean> memoryPoolBeans = ManagementFactory.getMemoryPoolMXBeans();
        memoryPoolBeans.forEach(memoryPoolMXBean -> {
            transportJvm.getHeapMemoryInfo().put(memoryPoolMXBean.getName().replaceAll(" ", ""), memoryPoolMXBean.getUsage());
        });
        //================jvm的内存状态================
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        //获取堆内存使用情况，包括初始大小，最大大小，已使用大小等，单位字节
        transportJvm.setHeapMemoryUsage(memoryBean.getHeapMemoryUsage());
        //获取堆外内存使用情况。
        transportJvm.setNonHeapMemoryUsage(memoryBean.getNonHeapMemoryUsage());
        return transportJvm;
    }
}
