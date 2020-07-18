package org.cloud.driver.model.list;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Classname UserIdList
 * @Description TODO
 * @Date 2020/6/13 17:28
 * @Created by 87454
 */
@Data
public class FileIdList implements Serializable {
    List<Long> fileIdList;
    int day;
}
