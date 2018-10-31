package cn.fireface.call.web.action;

import cn.fireface.call.core.proxy.CallChain;
import cn.fireface.call.web.service.HelloService;
import cn.fireface.call.web.uitls.RandomTime;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloAction extends ActionSupport{
    public String hello() throws InterruptedException {
        Thread.sleep(RandomTime.next());
        new HelloService().sayHello();
        new HelloService().sayBye();
        return SUCCESS;
    }

    public String hi() throws InterruptedException {
        Thread.sleep(RandomTime.next());
        new HelloService().sayHello();
        new HelloService().sayBye();
        return SUCCESS;
    }
}
