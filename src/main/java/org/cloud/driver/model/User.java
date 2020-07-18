package org.cloud.driver.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    //默认头像
    public static final String DEFAULT_ICONPATH="12";
    //默认等级
    public static int DEFAULT_RANK=1;
    private long id;
    private String username;
    private String email;
    private String password;
    private int sex;
    private String phone;
    private String iconPath;
    private Date applyTime;//注册时间
    private int rank;//用户等级
}
