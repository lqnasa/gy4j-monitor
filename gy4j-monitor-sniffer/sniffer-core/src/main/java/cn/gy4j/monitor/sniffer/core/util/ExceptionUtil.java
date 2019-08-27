package cn.gy4j.monitor.sniffer.core.util;

import cn.gy4j.monitor.sniffer.core.constant.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class ExceptionUtil {
    /**
     * 获取异常的堆栈信息.
     *
     * @param throwable 异常对象
     * @return
     */
    public static String format(Throwable throwable) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintWriter(buf, true));
        String expMeesage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.LINE_SEPARATOR + expMeesage;
    }
}
