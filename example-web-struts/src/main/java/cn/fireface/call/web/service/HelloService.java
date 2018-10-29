package cn.fireface.call.web.service;

import cn.fireface.call.web.manager.HelloManager;
import cn.fireface.call.core.proxy.CallChain;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloService {
    public void sayHello(){
        new HelloManager().sayHello();
        System.out.println("hello service  say : hello");
    }
}
