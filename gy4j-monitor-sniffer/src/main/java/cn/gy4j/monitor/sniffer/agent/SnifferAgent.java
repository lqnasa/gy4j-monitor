package cn.gy4j.monitor.sniffer.agent;

import java.lang.instrument.Instrumentation;

/**
 * javaagent代理入口
 * <p>
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class SnifferAgent {
    /**
     * 在方法在main方法之前执行，和main方法同Jvm、ClassLoader、Security policy和Context
     *
     * @param agentOps javaagent入参
     * @param inst     对class进行字节码加强的代理实例
     */
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("hello javaagent!this is premain!");
    }
}
