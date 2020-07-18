package org.cloud.driver.service;

import lombok.extern.slf4j.Slf4j;
import org.cloud.driver.Utils.UUIDUtil;
import org.cloud.driver.dao.DirectoryDao;
import org.cloud.driver.dao.FileDao;
import org.cloud.driver.dao.ShareDao;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.*;
import org.cloud.driver.model.list.FileIdList;
import org.cloud.driver.model.reponse.ShareReponse;
import org.cloud.driver.request.ShareRequest;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Classname ShareService
 * @Description TODO
 * @Date 2020/6/13 16:03
 * @Created by 87454
 */
@Service
@Slf4j
public class ShareService {
    @Autowired
    ShareDao shareDao;
    @Autowired
    FileDao fileDao;
    @Autowired
    DirectoryDao directoryDao;

    /**
     * 创建分享链接
     * @param user
     * @param fileIdList
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String createShareUrl(User user, FileIdList fileIdList) {
        if(fileIdList.getFileIdList().isEmpty()){
            ExceptionCast.cast(ResultCode.INVALID_PARAM);
        }
        List<Long> fidList = fileIdList.getFileIdList();

        Share share = new Share();
        share.setId(UUIDUtil.getUniqueKey());
        Date now = new Date();
        share.setCreate_time(now);
        share.setUser_id(user.getId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, fileIdList.getDay());
        Date endDate = calendar.getTime();
        share.setEnd_time(endDate);
        String url = Common.HTTP+ Common.WINDOWS_IP+Common.sharePath+share.getId();
        share.setUrl(url);
        for(long fid : fidList){
            int index = shareDao.addShareFile(share.getId(),fid);
            if(index < 1){
                ExceptionCast.cast(ResultCode.SHARE_CREATE_FAIL);
            }
        }
        int index = shareDao.addShare(share);
        if(index < 1){
            ExceptionCast.cast(ResultCode.SHARE_CREATE_URL_FAIL);
        }
        log.info("分享url为{}", url);
        return url;
    }

    /**
     * 获得分享文件
     * @param shareId
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ShareReponse getShare(long shareId) {
        Share share = shareDao.getById(shareId);
        if(share == null){
            ExceptionCast.cast(ResultCode.NO_FOUND);
        }
        if(share.getEnd_time().getTime() < new Date().getTime()){
            ExceptionCast.cast(ResultCode.SHARE_EXPIRE);
        }
        List<MyFile> myFiles = shareDao.getFilesByShareId(shareId);
        List<Directory> directories = shareDao.getDirectoriesByShareId(shareId);
        ShareReponse shareReponse = new ShareReponse();
        shareReponse.setShare(share);
        shareReponse.setMyFiles(myFiles);
        shareReponse.setDirectories(directories);
        return shareReponse;
    }

    /**
     * 保存分享的文件
     * @param user
     * @param shareRequest
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String saveFile(User user, ShareRequest shareRequest) {
        if(shareRequest == null){
            ExceptionCast.cast(ResultCode.INVALID_PARAM);
        }
        long directoryId = shareRequest.getDirectoryId();
        List<MyFile> myFiles = shareRequest.getShareReponse().getMyFiles();
        List<Directory> directories = shareRequest.getShareReponse().getDirectories();
        Date now = new Date();
        int fileindex = 0;
        //添加根目录文件
        for(MyFile myFile : myFiles){
            myFile.setUser_id(user.getId());
            myFile.setId(UUIDUtil.getUniqueKey());
            //数据库dbName
            String dbName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + myFile.getName();

            MyFile existFile = fileDao.getSameByDirectoryId(directoryId, myFile.getName(), user.getId());
            if(existFile != null){
                myFile.setName(dbName);
            }
            myFile.setDb_name(dbName);
            myFile.setDirectory_id(directoryId);
            myFile.setCreatetime(now);
            myFile.setCreatetime(now);
            fileDao.createFile(myFile);
            fileindex+=fileindex+fileDao.createFile(myFile);
        }
        //添加根目录文件夹
        int dirindex = 0;
        for(Directory directory : directories){
            directory.setUser_id(user.getId());
            directory.setId(UUIDUtil.getUniqueKey());

            Directory existDirectory = directoryDao.getSameByParentID(directoryId, directory.getName(), user.getId());
            if(existDirectory != null){
                String dbName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + directory.getName();
                directory.setName(dbName);
            }
            directory.setParent_id(directoryId);
            directory.setCreatetime(now);
            directory.setUpdatetime(now);
            dirindex+=directoryDao.createDirectory(directory);
        }

        List<Directory> subDirectories = findAllSubDirectory(directories,user.getId());
        List<MyFile> subFiles = findAllSubFiles(directories,user.getId());

        //添加子目录文件与文件夹
        for(MyFile myFile : myFiles){
            myFile.setUser_id(user.getId());
            myFile.setId(UUIDUtil.getUniqueKey());
            //数据库dbName
            String dbName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + myFile.getName();
            myFile.setDb_name(dbName);
            myFile.setCreatetime(now);
            myFile.setCreatetime(now);
            fileDao.createFile(myFile);
            fileindex+=fileindex+fileDao.createFile(myFile);
        }
        for(Directory directory : directories){
            directory.setUser_id(user.getId());
            directory.setId(UUIDUtil.getUniqueKey());

            directory.setParent_id(directoryId);
            directory.setCreatetime(now);
            directory.setUpdatetime(now);
            dirindex+=directoryDao.createDirectory(directory);
        }
        if(fileindex < 1 && dirindex < 1){
            ExceptionCast.cast(ResultCode.SHARE_SAVE_FAIL);
        }

        return "成功保存了"+dirindex+"个文件夹与"+fileindex+"个文件";
    }
    //找到所有子目录的子文件
    private List<MyFile> findAllSubFiles(List<Directory> directories, long userId){
        List<MyFile> res = new ArrayList<>();
        for(Directory directory : directories){
            List<MyFile> temp = fileDao.getAllByDirectoryID(directory.getId(), userId);
            res.addAll(res);
        }
        return res;
    }
    //bfs根据directory找到所有子目录
    private List<Directory> findSubDirectory(Directory directory, long userId){
        Queue<Directory> queue = new LinkedList<>();
        queue.offer(directory);
        List<Directory> res = new ArrayList<>();
        while(!queue.isEmpty()){
            Directory out = queue.poll();
            res.add(out);
            List<Directory> temps = directoryDao.getAllByParentID(out.getId(),userId);
            if(temps!=null) {
                for (Directory temp : temps) {
                    queue.offer(temp);
                }
            }
        }
        return res;
    }

    private List<Directory> findAllSubDirectory(List<Directory> directories, long userId){
        List<Directory> res = new ArrayList<>();
        for(Directory directory : directories){
            List<Directory> temp = directoryDao.getAllByParentID(directory.getId(), userId);
            res.addAll(res);
        }
        return res;
    }

}
