package org.cloud.driver.model.reponse;

import lombok.Data;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.MyFile;
import org.cloud.driver.model.Share;

import java.util.List;

/**
 * @Classname ShareReponse
 * @Description TODO
 * @Date 2020/6/13 22:40
 * @Created by 87454
 */
@Data
public class ShareReponse {
    Share share;
    List<MyFile> myFiles;
    List<Directory> directories;
}
