package org.cloud.driver.nano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Classname NeedLogin
 * @Description TODO
 * @Date 2020/6/9 11:59
 * @Created by 87454
 */
//https://www.cnblogs.com/baoyi/p/springboot_HandlerMethodArgumentResolver.html
//https://www.colabug.com/2020/0405/7208118/amp/
@Target(ElementType.PARAMETER)//注解类型
@Retention(RetentionPolicy.RUNTIME)//注解策略
public @interface NeedLogin {
}
