package org.cloud.driver.exception;

import com.google.common.collect.ImmutableMap;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //使用EXCEPTIONS存放异常类型和错误代码映射,ImmutableMap一旦创建不可改变(只读)
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTION;
    //builder用来构建map
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder =
            ImmutableMap.builder();

    // 捕获异常后处理的方法
    //捕获已经知异常
    @ExceptionHandler(CustomException.class)
    public Response customException(CustomException customException){
        LOGGER.error("catch exception:{}",customException);

        ResultCode resultCode = customException.resultCode;
        return new Response(resultCode);
    }
    //捕获不可知异常
    @ExceptionHandler(Exception.class)
    public Response exception(Exception exception){
        //记录日志
        LOGGER.error("catch exception:{}",exception);
        if(EXCEPTION == null){
            EXCEPTION = builder.build();
        }
        //从EXCEPTION中找异常类型对应的错误代码
        ResultCode resultCode = EXCEPTION.get(exception.getClass());
        if(resultCode != null){
            return new Response(resultCode);
        }else{
            return new Response(ResultCode.SERVER_ERROR);
        }
    }
    static{
        builder.put(HttpMessageNotReadableException.class, ResultCode.INVALID_PARAM);
    }
}
