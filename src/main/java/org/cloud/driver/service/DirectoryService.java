package org.cloud.driver.service;

import org.cloud.driver.Utils.UUIDUtil;
import org.cloud.driver.dao.DirectoryDao;
import org.cloud.driver.exception.CustomException;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.User;
import org.cloud.driver.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Classname DirectoryService
 * @Description TODO
 * @Date 2020/5/29 16:25
 * @Created by 87454
 */
@Service
public class DirectoryService {
    @Autowired
    DirectoryDao directoryDao;

    /**
     * 新建文件夹
     * @param directory
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Directory createDirectory(Directory directory, User user){
        if(directory == null){
            ExceptionCast.cast(ResultCode.DIRECTORY_PARAM_NULL);
        }
        String name = directory.getName();
        Directory curDir = directoryDao.getSameByParentID(directory.getParent_id(),name, user.getId());
        //如果当前目录存在同名目录
        if(curDir != null){
            String newName = name + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            directory.setName(newName);
        }
        directory.setId(UUIDUtil.getUniqueKey());
        Date now = new Date();
        directory.setCreatetime(now);
        directory.setUpdatetime(now);
        directory.setUser_id(user.getId());
        int index = 0;
        index = directoryDao.createDirectory(directory);
        if(index < 1) ExceptionCast.cast(ResultCode.DIRECTORY_CREATE_FAIL);
        return directory;
    }

    /**
     * 改名或者移动
     * @param directory
     * @return
     */
    @Transactional
    public Directory update(Directory directory, User user){
        if(directory == null){
            ExceptionCast.cast(ResultCode.NULL_PARAM);
        }
        //判断逻辑文件是否存在
        Directory updateDirectory = directoryDao.getById(directory.getId());
        if(updateDirectory == null){
            ExceptionCast.cast(ResultCode.DIRECTORY_NOT_EXIST);
        }
        //判断目录是否存在
        Directory existDirectory = directoryDao.getById(directory.getParent_id());
        if(existDirectory == null && directory.getParent_id() != 0){
            ExceptionCast.cast(ResultCode.DIRECTORY_NOT_EXIST);
        }
        //改名,新目录，新名字
        Directory existFile = directoryDao.getSameByParentID(directory.getParent_id(), directory.getName(), user.getId());
        if(existFile != null){
            ExceptionCast.cast(ResultCode.FILE_NAME_EXIST);
        }
        updateDirectory.setParent_id(directory.getParent_id());
        updateDirectory.setName(directory.getName());
        updateDirectory.setUpdatetime(new Date());
        int index = 0;
        //只更新三个字段
        index = directoryDao.updateDirectory(updateDirectory);
        if(index < 1){
            ExceptionCast.cast(ResultCode.DIRECTORY_CREATE_EXIST);
        }
        return updateDirectory;
    }

    /**
     * 删除文件夹
     * @param directoryId
     * @return
     */
    @Transactional
    public int deleteDirectory(long directoryId){
        int index = directoryDao.deleteDirectory(directoryId);
        if(index < 1){
            ExceptionCast.cast(ResultCode.FILE_DELETE_FAIL);
        }
        return index;
    }

    /**
     * 查找文件夹下所有文件夹
     * @param parentId
     * @return
     */
    public List<Directory> getByParentId(long parentId, User user){
        return directoryDao.getAllByParentID(parentId, user.getId());
    }

    /**
     * 获取回收站的所有文件
     * @return
     */
    public List<Directory> getDelete(User user) {
        return directoryDao.getStatusDelete(user.getId());
    }

    /**
     * 彻底删除回收站的某个文件夹
     * @param directoryId
     * @return
     */
    public int realDelete(long directoryId){
        int index = directoryDao.realDelete(directoryId);
        if(index < 1){
            ExceptionCast.cast(ResultCode.FILE_REAL_DELETE_FAIL);
        }
        return index;
    }
}
