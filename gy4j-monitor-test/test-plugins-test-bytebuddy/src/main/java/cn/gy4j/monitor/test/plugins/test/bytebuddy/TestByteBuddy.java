package cn.gy4j.monitor.test.plugins.test.bytebuddy;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-16
 */
public class TestByteBuddy {

    public void print() {
        new TestByteBuddy().print("hello trace!");
    }

    public void print(String msg) {
        System.out.println(msg);
    }

}
