package cn.gy4j.monitor.test.plugins.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
@Controller
public class DemoController {
    @Autowired
    private DemoService demoService;

    @RequestMapping("demo")
    @ResponseBody
    public String demo(String name) {
        return demoService.sayHello(name);
    }
}