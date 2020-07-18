package org.cloud.driver.exception;


import org.cloud.driver.response.ResultCode;

public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
