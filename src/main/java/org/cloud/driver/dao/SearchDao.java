package org.cloud.driver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.MyFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname SearchDao
 * @Description TODO
 * @Date 2020/6/15 16:36
 * @Created by 87454
 */
@Mapper
@Repository
public interface SearchDao {
    @Select("SELECT * FROM file WHERE `name` LIKE concat('%',#{key},'%') AND user_id = #{userId}")
    List<MyFile> searchFileByKey(@Param("key")String key, @Param("userId")long userid);

    @Select("SELECT * FROM directory WHERE `name` LIKE concat('%',#{key},'%') AND user_id = #{userId}")
            //AND user_id = #{userId}")
    List<Directory> searchDirectoryByKey(@Param("key")String key, @Param("userId") long userId);
}
