package org.cloud.driver.exception;

import org.cloud.driver.response.ResultCode;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {

    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
}
