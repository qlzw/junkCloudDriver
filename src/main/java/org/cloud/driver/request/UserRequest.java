package org.cloud.driver.request;

import lombok.Data;

//用户注册时的请求
@Data
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String code;
    private int sex;
    private String phone;
}
