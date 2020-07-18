package org.cloud.driver.request;

import lombok.Data;
import org.cloud.driver.model.reponse.ShareReponse;

/**
 * @Classname ShareRequest
 * @Description TODO
 * @Date 2020/6/15 15:37
 * @Created by 87454
 */
@Data
public class ShareRequest {
    ShareReponse shareReponse;
    long directoryId;
}
