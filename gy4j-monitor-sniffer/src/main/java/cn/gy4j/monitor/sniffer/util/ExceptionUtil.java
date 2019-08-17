package cn.gy4j.monitor.sniffer.util;

import cn.gy4j.monitor.sniffer.constant.Constants;

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
     * 获取异常的堆栈信息
     *
     * @param t
     * @return
     */
    public static String format(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new PrintWriter(buf, true));
        String expMeesage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.LINE_SEPARATOR + expMeesage;
    }
}
