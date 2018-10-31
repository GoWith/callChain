package cn.fireface.call.web.service;

import cn.fireface.call.web.manager.HelloManager;
import cn.fireface.call.core.proxy.CallChain;
import cn.fireface.call.web.uitls.RandomTime;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloService {
    public void sayHello() throws InterruptedException {
        new HelloManager().sayHello();
        new HelloManager().sayBye();
        Thread.sleep(RandomTime.next());
        System.out.println("hello service  say : hello");
    }
    public void sayBye() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new HelloManager().sayHello();
        }
        new HelloManager().sayHello();
        new HelloManager().sayBye();
        Thread.sleep(RandomTime.next());
        System.out.println("hello service  say : bye");
    }
}
