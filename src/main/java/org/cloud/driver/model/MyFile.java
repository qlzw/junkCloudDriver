package org.cloud.driver.model;

import lombok.Data;

import java.util.Date;

@Data
public class MyFile {
    private long id;
    private String db_name;
    private String name;
    private long directory_id;
    private String md5;
    private String size;
    private long real_size;
    private String suffix;
    private String type;
    private String path;
    private long user_id;
    private Date createtime;
    private Date updatetime;
    private int order_id;
    private String upload_ip;
    private int status;
}
