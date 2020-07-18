package org.cloud.driver.vo;

import lombok.Data;

/**
 * @Classname UserVo
 * @Description TODO
 * @Date 2020/6/10 10:50
 * @Created by 87454
 */
@Data
public class UserVo {
    private String email;
    private String phone;
    private String code;
    private String oldPassword;//旧密码
    private String newPassword;//新密码
    private String job;
}
