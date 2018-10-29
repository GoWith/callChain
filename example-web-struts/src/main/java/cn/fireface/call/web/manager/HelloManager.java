package cn.fireface.call.web.manager;

import cn.fireface.call.core.proxy.CallChain;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@CallChain
public class HelloManager {
    public void sayHello(){
        System.out.println("hello manager say : hello");
    }
}
