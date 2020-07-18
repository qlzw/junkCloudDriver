package org.cloud.driver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.MyFile;
import org.cloud.driver.model.User;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname SearchController
 * @Description TODO
 * @Date 2020/6/15 16:15
 * @Created by 87454
 */

@RestController
@Api(value = "/user",tags = "搜索模块", description = "用户搜索接口")
@RequestMapping("/cd/search")
@Slf4j
public class SearchController {
    @Autowired
    SearchService searchService;

    @ApiOperation(value = "根据文件名搜索",notes = "获取含有key的文件",httpMethod = "GET")
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public Response<List<MyFile>> fileSearch(User user, @RequestParam(value = "key", required = true)String key){
        List<MyFile> resultFiles = searchService.searchFileByName(user, key);
        return Response.SUCCESS(ResultCode.SUCCESS,resultFiles);
    }
    @ApiOperation(value = "根据文件夹名搜索",notes = "获取含有key的文件夹",httpMethod = "GET")
    @RequestMapping(value = "/directory", method = RequestMethod.GET)
    public Response<List<Directory>> directorySearch(User user, @RequestParam(value = "key", required = true)String key){
        List<Directory> resultFiles = searchService.searchDirectoryByName(key, user);
        return Response.SUCCESS(ResultCode.SUCCESS,resultFiles);
    }
}
