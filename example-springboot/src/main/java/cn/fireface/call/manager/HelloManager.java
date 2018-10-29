package cn.fireface.call.manager;

import cn.fireface.call.core.proxy.CallChain;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloManager {
    public void sayHello() throws InterruptedException {
        Thread.sleep(10L);
        System.out.println("hello manager say : hello");
    }
    public void sayBye() throws InterruptedException {
        Thread.sleep(10L);
        System.out.println("hello manager say : bye");
    }
}
