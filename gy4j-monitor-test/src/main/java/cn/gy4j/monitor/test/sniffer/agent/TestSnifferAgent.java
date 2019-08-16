package cn.gy4j.monitor.test.sniffer.agent;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class TestSnifferAgent {
    /**
     * 测试SnifferAgent的main方法
     * jvm配置:-javaagent:G:\gy4j\git_gy4j\gy4j-monitor\gy4j-monitor-sniffer\target\sniffer-agent.jar
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("hello javaagent!this is main!");
        new TestInstrumentation().print();
    }
}