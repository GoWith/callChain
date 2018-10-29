package cn.fireface.call.web.action;

import cn.fireface.call.web.service.HelloService;
import cn.fireface.call.core.proxy.CallChain;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloAction extends ActionSupport{
    public String hello(){
        new HelloService().sayHello();
        return "hello";
    }
}
