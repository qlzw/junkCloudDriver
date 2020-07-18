package org.cloud.driver.model;

import lombok.Data;

import java.util.Date;

@Data
public class Directory {
    private long id;
    private String name;
    private long parent_id;
    private long user_id;
    private Date createtime;
    private Date updatetime;
    private int status;
}
