package cn.fireface.call.example.compile;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
//@CallChain
public class ExampleService {
    public void example(){
        new ExampleManager().example();
        System.out.println("example");
    }
}
