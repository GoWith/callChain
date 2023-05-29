package cn.fireface.springboot.call.manager;

import cn.fireface.call.core.proxy.CallChain;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloManager {
    public void sayHello() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(10L);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println("hello manager say : hello");
    }
    public void sayBye() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(10L);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println("hello manager say : bye");
    }
}
