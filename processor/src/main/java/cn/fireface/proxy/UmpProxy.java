package cn.fireface.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by maoyi on 2018/10/23.
 * don't worry , be happy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface UmpProxy {
    String value() default "all is ready!";
}
