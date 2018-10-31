package cn.fireface.call.web.uitls;

import java.util.Random;

/**
 * Created by maoyi on 2018/10/31.
 * don't worry , be happy
 */
public class RandomTime {
    public static long next(){
        return new Random().nextInt(1000);
    }
}
