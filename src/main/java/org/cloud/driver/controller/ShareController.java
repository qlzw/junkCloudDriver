package org.cloud.driver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.Share;
import org.cloud.driver.model.User;
import org.cloud.driver.model.list.FileIdList;
import org.cloud.driver.model.reponse.ShareReponse;
import org.cloud.driver.request.ShareRequest;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname ShareController
 * @Description TODO
 * @Date 2020/6/14 11:16
 * @Created by 87454
 */
@RestController
@RequestMapping("/cd/share")
@Api(value = "/share", tags = "分享模块",description = "分享接口")
public class ShareController {
    @Autowired
    ShareService shareService;

    @ApiOperation(value = "创建分享链接",notes = "传递所有文件id以及结束时间datetime",httpMethod = "POST")
    @RequestMapping(value = "/createUrl", method = RequestMethod.POST)
    public Response<String> createUrl(User user, @RequestBody FileIdList fileIdList) {
        String url = shareService.createShareUrl(user, fileIdList);
        //文件上传成功成功返回
        return Response.SUCCESS(ResultCode.SUCCESS, url);
    }

    @ApiOperation(value = "根据链接获得对象",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/get/{shareId}", method = RequestMethod.GET)
    public Response<ShareReponse> getShare(@PathVariable("shareId") long shareId) {
        ShareReponse shareReponse = shareService.getShare(shareId);
        //文件上传成功成功返回
        return Response.SUCCESS(ResultCode.SUCCESS, shareReponse);
    }

    @ApiOperation(value = "保存分享的文件",notes = "暂无",httpMethod = "POST")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Response<String> saveShare(User user, @RequestBody ShareRequest shareRequest) {
        String res = shareService.saveFile(user, shareRequest);
        return Response.SUCCESS(ResultCode.SUCCESS, res);
    }
}
