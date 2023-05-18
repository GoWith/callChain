package cn.fireface.springboot.call.service;

import cn.fireface.call.core.proxy.CallChain;
import cn.fireface.springboot.call.manager.HelloManager;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloService {
    public void sayHello() throws InterruptedException {
        new HelloManager().sayHello();
        new HelloManager().sayBye();
        Thread.sleep(10L);
        System.out.println("hello service  say : hello");
    }
    public void sayBye() throws InterruptedException {
        new HelloManager().sayHello();
        new HelloManager().sayBye();
        Thread.sleep(10L);
        System.out.println("hello service  say : bye");
    }
}
