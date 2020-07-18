package org.cloud.driver.model;

import lombok.Data;

import java.util.Date;

/**
 * @Classname Share
 * @Description TODO
 * @Date 2020/6/13 19:39
 * @Created by 87454
 */
@Data
public class Share {
    private long id;
    private Date create_time;
    private Date end_time;
    private String url;
    private long user_id;
}
