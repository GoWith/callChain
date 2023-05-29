package cn.fireface.springboot.call.service;

import cn.fireface.call.core.proxy.CallChain;
import cn.fireface.springboot.call.manager.HelloManager;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloService2 {
    public void sayHello() throws InterruptedException {
        new HelloManager().sayHello();
        new HelloManager().sayBye();
        long start = System.currentTimeMillis();
        Thread.sleep(10L);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println("hello service  say : hello");
    }
    public void sayBye() throws InterruptedException {
        new HelloManager().sayHello();
        new HelloManager().sayBye();
        long start = System.currentTimeMillis();
        Thread.sleep(10L);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println("hello service  say : bye");
    }
}
