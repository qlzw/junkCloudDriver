package org.cloud.driver.service;

import org.cloud.driver.Utils.*;
import org.cloud.driver.dao.DirectoryDao;
import org.cloud.driver.dao.FileDao;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.Common;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.MyFile;
import org.cloud.driver.model.User;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname FileService
 * @Description TODO
 * @Date 2020/5/28 16:44
 * @Created by 87454
 */
@Service
public class FileService {
    @Autowired
    FileDao fileDao;
    @Autowired
    DirectoryDao directoryDao;


    /**
     * 上传文件
     * @param request
     * @param file
     * @param fileModel
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public MyFile upload(HttpServletRequest request, MultipartFile file, MyFile fileModel, User user) {
        if (file.isEmpty()) {
            //上传文件为空直接抛出
            ExceptionCast.cast(ResultCode.FILE_UPLOAD_EMPTY);
        }
        if(fileModel==null){
            ExceptionCast.cast(ResultCode.INVALID_PARAM);
        }
        //获取文件原名
        String realName = file.getOriginalFilename();
        //获取后缀名
        String[] suffix = realName.split("\\.");
        //
        String regex = "";
        if(suffix.length >= 2)
             regex = suffix[suffix.length-1];
        fileModel.setName(realName);
        //数据库dbName
        String dbName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + realName;
        //路径
        //System.out.println(dbName);
        String path = Common.LINUX_REAL_PATH + dbName;
        File writeFile = new File(path);
        OutputStream os = null;
        InputStream is = null;
        try {
            //判断真实文件是否已经存在
            if (writeFile.exists()) {
                ExceptionCast.cast(ResultCode.FILE_UPLOAD_EXIST);
            }
            //判断逻辑文件是否存在
            MyFile existFile = fileDao.getSameByDirectoryId(fileModel.getDirectory_id(), fileModel.getName(), user.getId());
            if(existFile != null){
                fileModel.setName(dbName);
            }
            is = file.getInputStream();
            os = new FileOutputStream(writeFile);
            int n = 0;
            byte[] bb = new byte[1024];
            while ((n = is.read(bb)) != -1) {
                os.write(bb, 0, n);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ExceptionCast.cast(ResultCode.FILE_UPLOAD_FAIL);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(ResultCode.FILE_UPLOAD_FAIL);
        }finally {
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Date now = new Date();
        fileModel.setId(UUIDUtil.getUniqueKey());
        fileModel.setDb_name(dbName);
        fileModel.setMd5(FileUtils.getFileMD5(is));
        fileModel.setReal_size(file.getSize());
        fileModel.setSize(CovertUtil.size2String(file.getSize()));
        fileModel.setPath(writeFile.getPath());
        fileModel.setUpload_ip(request.getRemoteAddr());
        fileModel.setSuffix(regex);
        fileModel.setType(TypeCovertUtil.getType(regex));
        fileModel.setCreatetime(now);
        fileModel.setUpdatetime(now);
        fileModel.setUser_id(user.getId());
        fileModel.setStatus(0);

        int index =fileDao.createFile(fileModel);
        if(index < 1){
            ExceptionCast.cast(ResultCode.FILE_UPLOAD_FAIL);
        }
        return fileModel;
    }

    /**
     * 获取文件夹下所有文件
     * @param directoryId
     * @return
     */
    public List<MyFile> getByDirectory(long directoryId, User user){
        return fileDao.getAllByDirectoryID(directoryId, user.getId());
    }

    /**
     * 获取某种类型下所有文件
     * @param type
     * @return
     */
    public List<MyFile> getByType(String type, User user){
        return fileDao.getAllByType(type, user.getId());
    }

    /**
     * 下载文件
     * @param response
     * @param fileId
     */
    public void download(HttpServletResponse response, long fileId){
        MyFile myFile = fileDao.getById(fileId);
        if(myFile == null){
            ExceptionCast.cast(ResultCode.FILE_NOT_EXIST);
        }
        File file = new File(myFile.getPath());
        if(!file.exists()){
            ExceptionCast.cast(ResultCode.FILE_NOT_EXIST);
        }
        String name = myFile.getName();
        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + name);// 设置文件名
        //System.out.println(name);
        //System.out.println(response.getContentType());

        byte[] buffer = new byte[1024];
        InputStream fis = null;
        BufferedInputStream bis = null;

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int n = 0;
            while((n = bis.read(buffer)) != -1){
                os.write(buffer, 0,  n);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ExceptionCast.cast(ResultCode.FILE_NOT_EXIST);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(ResultCode.SERVER_ERROR);
        }finally {
                try {
                    if(bis != null)  bis.close();
                    if(fis != null)  fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    //文件名相同文件相同不写入，文件名相同文件不相同要写入，

    /**
     * 改名或者移动
     * @param file
     * @return
     */
    @Transactional
    public MyFile update(MyFile file, User user){
        if(file == null){
            ExceptionCast.cast(ResultCode.NULL_PARAM);
        }
        //判断逻辑文件是否存在
        MyFile updateFile = fileDao.getById(file.getId());
        if(updateFile == null){
            ExceptionCast.cast(ResultCode.FILE_NOT_EXIST);
        }
        //判断目录是否存在
        Directory existDirectory = directoryDao.getById(file.getDirectory_id());
        if(existDirectory == null && file.getDirectory_id() == 0){
            ExceptionCast.cast(ResultCode.DIRECTORY_NOT_EXIST);
        }
        //改名,新目录，新名字
        MyFile existFile = fileDao.getSameByDirectoryId(file.getDirectory_id(), file.getName(), user.getId());
        if(existFile != null){
            ExceptionCast.cast(ResultCode.FILE_NAME_EXIST);
        }
        updateFile.setDirectory_id(file.getDirectory_id());
        updateFile.setName(file.getName());
        updateFile.setUpdatetime(new Date());
        int index = 0;
        index = fileDao.updateFile(updateFile);
        if(index < 1){
            ExceptionCast.cast(ResultCode.FILE_UPDATE_FAIL);
        }
        return updateFile;
    }

    /**
     * 删除文件
     * @param fileId
     * @return
     */
    @Transactional
    public int delete(long fileId){
        int index = fileDao.deleteFile(fileId);
        if(index < 1){
            ExceptionCast.cast(ResultCode.FILE_DELETE_FAIL);
        }
        return index;
    }

    /**
     * 获取回收站文件
     * @return
     */
    @Transactional
    public List<MyFile> getDelete(User user){
        return fileDao.getStatusDelete(user.getId());
    }

    /**
     * 彻底删除回收站的某个文件
     * @param fileId
     * @return
     */
    public int realDelete(long fileId){
        int index = fileDao.realDelete(fileId);
        if(index < 1){
            ExceptionCast.cast(ResultCode.FILE_REAL_DELETE_FAIL);
        }
        return index;
    }

    /**
     * 清空回收站
     * @return
     */
    public int realAllDelete(User user){
        int index = fileDao.realALLDelete(user.getId());
        return index;
    }

    /**
     * 获取已用容量百分比
     * @return
     */
    public long getCapacityByPercent(User user){
        long totalCap_B = fileDao.getCapacity(user.getId());
        return totalCap_B;
    }
/**
    public static void main(String[] args) {
        //同文件名的相同文件不会覆盖只会访问，相同名的不同文件会覆盖
        String realName = "haha1.png";
        String path = Common.WINDOWS_REAL_PATH + realName;
        File readfile = new File("D:/hehe.txt");
        File writefile = new File(path);
        System.out.println(readfile.length());
        System.out.println(writefile.length());
        System.out.println(CovertUtil.size2String(readfile.length()));
        FileInputStream fis = null;
        OutputStream os = null;
        FileInputStream exitsts_is = null;
        try {
            fis = new FileInputStream(readfile);
            //os = new FileOutputStream(path);
            //如果要写的文件名存在(两种可能：1文件存在、2文件同名，但是不存在)
            if (writefile.exists()) {
                writefile = FileUtils.rename(writefile, readfile, 1);
            }
            //文件已存在
            if (writefile == null) {
                return;
            }
            os = new FileOutputStream(writefile);

            int n = 0;
            byte[] bb = new byte[1024];
            while ((n = fis.read(bb)) != -1) {
                os.write(bb, 0, n);
            }
            System.out.println("写入成功");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
 **/
}
