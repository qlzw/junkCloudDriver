package org.cloud.driver.vo;

import lombok.Data;

@Data
public class LoginVo {
    //@NotNull
    private String email;
    //@NotNull
    //@Length(min=6,max=15)//密码长度不小于6
    private String password;
    private String verifyCode;
    //private String username;
}
