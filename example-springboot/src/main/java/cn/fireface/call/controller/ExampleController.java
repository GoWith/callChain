package cn.fireface.call.controller;

import cn.fireface.call.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@Controller
public class ExampleController {
    @RequestMapping("/")
    public String index() throws InterruptedException {
        new HelloService().sayHello();
        new HelloService().sayBye();
        return "index";
    }
}
