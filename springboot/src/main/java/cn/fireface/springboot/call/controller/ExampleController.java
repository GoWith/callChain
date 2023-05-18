package cn.fireface.springboot.call.controller;

import cn.fireface.springboot.call.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@Controller
public class ExampleController {
    @RequestMapping("/index")
    public String index() throws InterruptedException {
        new HelloService().sayHello();
        new HelloService().sayBye();
        return "index";
    }
}
