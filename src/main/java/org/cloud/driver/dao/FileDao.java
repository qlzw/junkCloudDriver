package org.cloud.driver.dao;

import org.apache.ibatis.annotations.*;
import org.cloud.driver.model.MyFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *2020年5月28日15:56:12
 */
@Repository
@Mapper
public interface FileDao {
    //新增文件
    @Insert("INSERT INTO `file` VALUES(#{id},#{db_name},#{name},#{directory_id},#{md5},#{size},#{real_size}," +
            "#{type},#{suffix},#{path},#{user_id},#{createtime},#{updatetime},#{order_id},#{upload_ip}," +
            "#{status})")
    int createFile(MyFile file);

    //彻底删除文件
    @Delete("DELETE FROM `file` WHERE id = #{id} and status = -1")
    int realDelete(@Param("id") long fileId);

    //彻底所有回收站的文件
    @Delete("DELETE f,d FROM `file` as f,`directory` as d WHERE f.`status` = -1 AND d.`status` = -1 AND user_id = #{userId}")
    int realALLDelete(@Param("userId") long userId);

    @Update("UPDATE `file` SET name = #{name}, directory_id = #{directory_id} " +
            ", updatetime = #{updatetime} WHERE id = #{id}")
    int updateFile(MyFile file);
    //删除文件
    @Update("UPDATE `file` SET status = -1 WHERE id = #{id}")
    int deleteFile(@Param("id") long fileId);
    //获取文件夹下所有文件
    @Select("SELECT * FROM `file` WHERE directory_id = #{directoryId} and user_id = #{userId} and status = 0" )
    List<MyFile> getAllByDirectoryID(@Param("directoryId") long directoryId, @Param("userId") long userId);

    //根据文件类型获取文件
    @Select("SELECT * FROM `file` WHERE type = #{type} and user_id = #{userId} and status = 0" )
    List<MyFile> getAllByType(@Param("type") String type, @Param("userId") long userId);

    //查找文件夹下名称相同的文件是否存在
    @Select("SELECT * FROM `file` WHERE status = 0 and directory_id = #{directoryId} and name = #{name} and user_id = #{userId}" )
    MyFile getSameByDirectoryId(@Param("directoryId") long directoryId, @Param("name") String name, @Param("userId") long userId);

    //根据文件id获取文件
    @Select("SELECT * FROM `file` WHERE id = #{id}")
    MyFile getById(@Param("id") long id);

    //获取已删除的文件
    @Select("SELECT * FROM `file` WHERE status = -1 and user_id = #{userId}")
    List<MyFile> getStatusDelete(@Param("userId") long userId);

    //获取数据库文件总大小
    @Select("SELECT SUM(real_size) FROM file WHERE user_id = #{userId}")
    long getCapacity(@Param("userId") long userId);
}
