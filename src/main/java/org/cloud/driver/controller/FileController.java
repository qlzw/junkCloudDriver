package org.cloud.driver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.Common;
import org.cloud.driver.model.MyFile;
import org.cloud.driver.model.User;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname FileController
 * @Description TODO
 * @Date 2020/5/29 15:16
 * @Created by 87454
 */
@RestController
@RequestMapping("/cd/file")
@Api(value = "/file", tags = "文件模块",description = "文件接口")
public class FileController {
    @Autowired
    FileService fileService;

    @ApiOperation(value = "上传文件",notes = "传所在文件夹id参数",httpMethod = "POST")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Response<MyFile> fileUpload(HttpServletRequest request, @Param("file") MultipartFile file, MyFile fimeModel, User user) throws IOException {
        MyFile resultFile= fileService.upload(request,file,fimeModel,user);
        //文件上传成功成功返回
        return Response.SUCCESS(ResultCode.OK, fimeModel);
    }

    @ApiOperation(value = "更改文件目录或者文件名",notes = "更改文件目录或者文件名",httpMethod = "POST")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response<MyFile> fileUpdate(@RequestBody MyFile file, User user){
        MyFile resultFile = fileService.update(file, user);
        return Response.SUCCESS(ResultCode.SUCCESS,resultFile);
    }

    @ApiOperation(value = "下载文件",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
    public void fileDownload(HttpServletResponse response,@PathVariable("fileId") long fileId){
        fileService.download(response,fileId);
    }

    @ApiOperation(value = "查找文件夹下面的所有文件",notes = "根目录的文件夹id为0",httpMethod = "GET")
    @RequestMapping(value = "/getFiles/{directoryId}", method = RequestMethod.GET)
    public Response<List<MyFile>> getFiles(@PathVariable("directoryId") long directoryId, User user){
        List<MyFile> files = new ArrayList<>();
        files = fileService.getByDirectory(directoryId, user);
        return Response.SUCCESS(ResultCode.OK,files);
    }

    @ApiOperation(value = "获取某类型的所有文件",notes = "根目录的文件夹id为0",httpMethod = "GET")
    @RequestMapping(value = "/getByType", method = RequestMethod.GET)
    public Response<List<MyFile>> getFilesByType(@RequestParam(name = "type", required = true) String type, User user){
        List<MyFile> files = new ArrayList<>();
        files = fileService.getByType(type,user);
        return Response.SUCCESS(ResultCode.OK,files);
    }

    @ApiOperation(value = "删除文件",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/delete/{fileId}", method = RequestMethod.GET)
    public Response<String> deleteFiles(@PathVariable("fileId") long fileId){
        int index = fileService.delete(fileId);
        String res = "成功将"+index+"个文件放入回收站";
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "获取回收站文件",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/getDelete", method = RequestMethod.GET)
    public Response<List<MyFile>> getDeleteFiles(User user){
        List<MyFile> res = fileService.getDelete(user);
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "彻底删除回收站的单个文件",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/realDelete/{fileId}", method = RequestMethod.GET)
    public Response<String> realDelete(@PathVariable("fileId") long fileId){
        int index = fileService.delete(fileId);
        String res = "成功彻底删除"+index+"个文件";
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "清空回收站",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/realAllDelete", method = RequestMethod.GET)
    public Response<String> realAllDelete(User user){
        int index = fileService.realAllDelete(user);
        String res = "成功清空"+index+"个文件";
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "获取已占用容量",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/getUsedSpace", method = RequestMethod.GET)
    public Response<Long> getUsedSpace(User user){
        long res = fileService.getCapacityByPercent(user);
        return Response.SUCCESS(ResultCode.OK,res);
    }

    @ApiOperation(value = "获取总容量",notes = "暂无",httpMethod = "GET")
    @RequestMapping(value = "/getAllSpace", method = RequestMethod.GET)
    public Response<Long> getAllSpace(User user){
        long res = Common.maxCap_B;
        return Response.SUCCESS(ResultCode.OK,res);
    }
}
