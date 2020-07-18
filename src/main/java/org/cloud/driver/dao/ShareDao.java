package org.cloud.driver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.MyFile;
import org.cloud.driver.model.Share;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname ShareDao
 * @Description TODO
 * @Date 2020/6/13 20:42
 * @Created by 87454
 */
@Repository
@Mapper
public interface ShareDao {
    //插入file
    @Insert("INSERT INTO share_file(`share_id`, `file_id`) VALUES(#{shareId},#{fileId})")
    int addShareFile(@Param("shareId") long shareId, @Param("fileId") long fileId);
    //插入share
    @Insert("INSERT INTO `share` VALUES(#{id},#{create_time},#{end_time},#{url},#{user_id})")
    int addShare(Share share);



    @Select("SELECT * FROM `share` WHERE id = #{id}")
    Share getById(@Param("id")long id);

    //根据shareId查找file
    @Select("select f.* from `share_file` as sf LEFT JOIN `share` as s on sf.share_id = s.id " +
            "LEFT JOIN `file` as f on sf.file_id = f.id WHERE f.id is not null and s.id = #{shareId}")
    List<MyFile> getFilesByShareId(@Param("shareId")long shareId);

    //根据shareId查找directory
    @Select("select d.* from `share_file` as sf LEFT JOIN `share` as s on sf.share_id = s.id " +
            "LEFT JOIN `directory` as d on sf.file_id = d.id WHERE d.id is not null and s.id = #{shareId}")
    List<Directory> getDirectoriesByShareId(@Param("shareId")long shareId);
}
