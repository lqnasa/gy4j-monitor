package cn.gy4j.monitor.test.plugins.spring.mvc.annotation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
@Controller
@RequestMapping("hello")
public class HelloController {
    @RequestMapping("say")
    @ResponseBody
    public String sayHello(String name) {
        return "hello " + name;
    }
}
