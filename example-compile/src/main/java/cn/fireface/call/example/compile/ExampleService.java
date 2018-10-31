package cn.fireface.call.example.compile;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
//@CallChain
public class ExampleService {
    public void example(){
        try {
            new ExampleManager().example();
            System.out.println("example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String example(String k){
        try {
            new ExampleManager().example();
            System.out.println("example");
            return "kk";
        } catch (Exception e) {
            e.printStackTrace();
            return "qq";
        }
    }

    public String example1(String k){
        long l = System.currentTimeMillis() % 3;
        if (l==1){
            long a = 2L;
            System.out.println("ewew");
            return "1";
        }else if (l==2){
            return "2";
        }else {
            return "3";
        }
    }

    public String example2(String k){
        long l = System.currentTimeMillis() % 3;
        if (l==1){
            try{
                return "1";
            }catch (Exception e){
                return "e";
            }
        }else if (l==2){
            for (int i = 0; i < 2; i++) {
                return "2";
            }
            return "2";
        }else {
            return "3";
        }
    }
}
