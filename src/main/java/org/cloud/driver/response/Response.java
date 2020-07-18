package org.cloud.driver.response;

public class Response<T> {

    boolean success;
    int code;
    String message;
    T data;

    public Response(ResultCode resultCode){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public Response(ResultCode resultCode, T data){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    /**
     * 成功
     * @param resultCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Response<T> SUCCESS(ResultCode resultCode,T data){
        return new Response<T>(resultCode, data);
    }
    /**
     * 成功
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Response<T> SUCCESS(ResultCode resultCode){
        return new Response<T>(resultCode);
    }

    /**
     * 失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Response<T> FAIL(ResultCode resultCode){
        return new Response<T>(resultCode);
    }
    /**
     * 失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Response<T> FAIL(ResultCode resultCode,T data){
        return new Response<T>(resultCode, data);
    }

    /**
     * 异常/错误
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Response<T> ERROR(ResultCode resultCode){
        return new Response<T>(resultCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
