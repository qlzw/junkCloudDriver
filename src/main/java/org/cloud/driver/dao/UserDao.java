package org.cloud.driver.dao;

import org.apache.ibatis.annotations.*;
import org.cloud.driver.model.User;
import org.springframework.stereotype.Repository;

/**
 *2020年5月22日16:57:24
 */
@Repository
@Mapper
public interface UserDao {
    //用户注册
    @Insert("INSERT INTO `user` VALUES(#{id},#{username},#{email}," +
            "#{password},#{sex},#{phone},#{iconPath},#{applyTime},#{rank})")
    int register(User user);

    @Update("UPDATE `user` SET `password` = #{password}")
    int updatePassword(@Param("password") String password);

    @Select("SELECT * FROM `user` WHERE `email` = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("SELECT * FROM `user` WHERE `username` = #{username}")
    User getByUsername(@Param("username") String username);
}
