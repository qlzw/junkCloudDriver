package org.cloud.driver.response;

/**
 * 10000-- 通用错误代码
 */
public enum ResultCode {
    //通用码
    OK(true, 200, "操作成功"),
    CREATED(true, 201, "创建成功"),
    ACCEPTED(true, 202, "请求已接受(排队中)"),
    NO_CONTENT(true, 204, "内容已删除"),
    REDIRECT(true, 306, "重定向"),
    INVALID_REQUEST(false, 400, "请求方式错误"),
    UNAUTHORIZED(false, 401, "用户未认证"),
    FORBIDDEN(false, 403, "禁止用户访问"),
    NO_FOUND(false, 404, "内容不存在"),
    NOT_ACCEPTABLE(false, 406, "请求格式错误"),
    SERVER_ERROR(false, 500, "服务端异常"),
    //用户模块
    USER_LOGIN_SUCESS(true, 100010, "用户登陆成功"),
    USER_PARAM_NULL(false, 10001, "用户请求参数为空"),
    USER_EMAIL_FAIL(false, 10002, "验证邮件发送失败"),
    USER_EMAIL_EXIST(false, 10003, "邮箱已注册"),
    USER_USERNAME_EXIST(false, 10004, "用户名已存在"),
    USER_REGISTER_FAIL(false, 10005, "用户注册失败"),
    USER_EMAIL_ERROR(false, 10006, "邮箱输入错误"),
    USER_PASSWORD_ERROR(false, 10007, "密码输入错误"),
    USER_OLD_PASSWORD_ERROR(false, 10012, "原密码输入错误"),
    USER_CHANGE_PASSWORD_ERROR(false, 10011, "用户更改密码失败"),
    USER_CODE_ERROR(false, 10008, "验证码校验错误"),
    USER_NOT_LOGIN(false, 10009, "用户信息不存在或者已经失效,请重新登录"),
    USER_TOKEN_ERROR(false, 10010, "用户信息已过期,请重新登录"),
    USER_NOT_EXIST(false, 10013, "用户不存在"),
    USER_CODE_CREATE_FAIL(false, 100014, "登陆验证码生成失败"),
    USER_CODE_VERIFY_FAIL(false, 100015, "登陆验证码校验失败"),
    //文件模块
    FILE_UPLOAD_EMPTY(false,20001,"上传文件为空"),
    FILE_UPLOAD_EXIST(false, 20002, "上传文件已存在"),
    FILE_UPLOAD_FAIL(false, 20003, "文件上传失败"),
    FILE_NOT_EXIST(false, 20004, "文件不存在"),
    FILE_NAME_EXIST(false, 20005, "文件名已存在"),
    FILE_UPDATE_FAIL(false, 20006, "文件更新失败"),
    FILE_DOWNLOAD_FAIL(false, 20007, "文件下载失败"),
    FILE_DELETE_FAIL(false, 20008, "文件删除失败"),
    FILE_REAL_DELETE_FAIL(false, 20009, "文件彻底删除失败"),
    DIRECTORY_PARAM_NULL(false,200020,"文件夹请求参数为空"),
    DIRECTORY_CREATE_FAIL(false,200021,"文件夹创建失败"),
    DIRECTORY_CREATE_EXIST(false,200022,"文件夹已存在"),
    DIRECTORY_NOT_EXIST(false,200023,"文件夹不存在"),
    //分享模块
    SHARE_CREATE_FAIL(false,30001,"文件分享失败"),
    SHARE_CREATE_URL_FAIL(false,30002,"分享链接创建失败"),
    SHARE_EXPIRE(false,30003,"文件分享已过期"),
    SHARE_SAVE_FAIL(false,30004,"文件保存失败"),
    //通用模块
    SUCCESS(true,200,"操作成功"),
    NULL_PARAM(false,406,"请求参数为控"),
    INVALID_PARAM(false,10000,"请求参数非法");

    //操作是否成功
    private boolean success;
    //操作代码
    private int code;
    //提示信息
    private String message;

    ResultCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
