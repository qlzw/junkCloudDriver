package org.cloud.driver.dao;

import org.apache.ibatis.annotations.*;
import org.cloud.driver.model.Directory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname DirectoryDao
 * @Description TODO
 * @Date 2020/5/28 16:17
 * @Created by 87454
 */
@Repository
@Mapper
public interface DirectoryDao {
    @Insert("INSERT INTO `directory` VALUES(#{id},#{name},#{parent_id}," +
            "#{user_id},#{createtime},#{updatetime},#{status})")
    int createDirectory(Directory directory);

    //彻底删除文件夹
    @Delete("DELETE FROM `directory` WHERE id = #{id} and status = -1")
    int realDelete(@Param("id") long directoryId);

    @Update("UPDATE `directory` SET name = #{name}, parent_id = #{parent_id}, updatetime = #{updatetime}" +
            "WHERE id = #{id}")
    int updateDirectory(Directory directory);

    //删除文件
    @Update("UPDATE `directory` SET status = -1 WHERE id = #{id}")
    int deleteDirectory(@Param("id") long directoryId);

    //获取文件夹下所有文件夹
    @Select("SELECT * FROM `directory` WHERE parent_id = #{parentId} and status = 0 and user_id = #{userId}" )
    List<Directory> getAllByParentID(@Param("parentId") long parentId, @Param("userId") long userId);

    //查找文件夹下名称相同的文件是否存在
    @Select("SELECT * FROM `directory` WHERE status = 0 and parent_id = #{parentId} and name = #{name} and user_id = #{userId}" )
    Directory getSameByParentID(@Param("parentId") long parentId,@Param("name") String name, @Param("userId") long userId);

    //根据文件id获取文件
    @Select("SELECT * FROM `directory` WHERE id = #{id}")
    Directory getById(@Param("id") long id);



    //获取已删除的文件夹
    @Select("SELECT * FROM `directory` WHERE status = -1 and user_id = #{userId}")
    List<Directory> getStatusDelete(@Param("userId") long userId);

}
