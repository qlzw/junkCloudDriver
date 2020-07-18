package org.cloud.driver.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.User;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Classname DirectoryController
 * @Description TODO
 * @Date 2020/5/29 16:38
 * @Created by 87454
 */
@RestController
@Api(value = "文件夹接口",tags = "文件夹模块", description = "文件夹接口")
@RequestMapping("/cd/directory")
public class DirectoryController {
    @Autowired
    DirectoryService directoryService;

    @ApiOperation(value = "新建文件夹",notes = "json传name参数",httpMethod = "POST")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response<Directory> create(@RequestBody Directory directory, User user) {
        Directory resultDirectory = directoryService.createDirectory(directory, user);
        return Response.SUCCESS(ResultCode.OK, resultDirectory);
    }

    @ApiOperation(value = "查找文件夹下面的所有文件夹",notes = "根目录的文件夹id为-1",httpMethod = "GET")
    @RequestMapping(value = "/getFiles/{directoryId}", method = RequestMethod.GET)
    public Response<List<Directory>> getFiles(@PathVariable("directoryId") long parentId, User user) {
        List<Directory> directories = new ArrayList<>();
        directories = directoryService.getByParentId(parentId,user);
        return Response.SUCCESS(ResultCode.OK, directories);
    }

    @ApiOperation(value = "更改文件夹目录或者文件夹名",notes = "更改文件夹目录或者文件夹名",httpMethod = "POST")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response<Directory> fileUpdate(@RequestBody Directory directory, User user){
        Directory resultFile = directoryService.update(directory, user);
        return Response.SUCCESS(ResultCode.SUCCESS,resultFile);
    }

    @ApiOperation(value = "删除文件夹",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/delete/{directoryId}", method = RequestMethod.GET)
    public Response<String> deleteFiles(@PathVariable("directoryId") long directoryId){
        int index = directoryService.deleteDirectory(directoryId);
        String res = "成功将"+index+"个文件夹放入回收站";
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "获取回收站的文件夹",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/getDelete", method = RequestMethod.GET)
    public Response<List<Directory>> getDeleteFiles(User user){
        List<Directory> res = directoryService.getDelete(user);
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "彻底删除单个文件夹",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/realDelete/{directoryId}", method = RequestMethod.GET)
    public Response<String> realDelete(@PathVariable("directoryId") long directoryId){
        int index = directoryService.realDelete(directoryId);
        String res = "成功彻底删除"+index+"个文件夹";
        return Response.SUCCESS(ResultCode.OK,res);
    }

}
