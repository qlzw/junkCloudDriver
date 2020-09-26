package org.cloud.driver.service;

import org.cloud.driver.Utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Test
    public void print() throws FileNotFoundException {
        File readfile = new File("D:/CloudDriver/uploadFile/20200616210907_实验5 HTTP协议分析实验.pdf");
        FileInputStream fis = new FileInputStream(readfile);
        System.out.println(FileUtils.getFileMD5(fis));
    }
}
