package cn.gy4j.monitor.sniffer.agent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

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
        inst.addTransformer(new Transformer());
    }

    static class Transformer implements ClassFileTransformer {
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined
                , ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className.equals("cn/gy4j/monitor/test/sniffer/agent/TestInstrumentation")) {
                // TestInstrumentation.class.2(TestInstrumentation的print方法为System.out.println(2);的class文件)
                InputStream inputStream = loader.getResourceAsStream("TestInstrumentation.class.2");
                try {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int n = 0;
                    while (-1 != (n = inputStream.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                    return output.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
