package org.cloud.driver.service;

import org.cloud.driver.dao.SearchDao;
import org.cloud.driver.model.Directory;
import org.cloud.driver.model.MyFile;
import org.cloud.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname SearchService
 * @Description TODO
 * @Date 2020/6/15 16:16
 * @Created by 87454
 */
@Service
public class SearchService {
    @Autowired
    SearchDao searchDao;
    public List<MyFile> searchFileByName(User user, String key){
        return searchDao.searchFileByKey(key, user.getId());
    }
    public List<Directory> searchDirectoryByName(String key, User user){
        return searchDao.searchDirectoryByKey(key, user.getId());
    }
}
