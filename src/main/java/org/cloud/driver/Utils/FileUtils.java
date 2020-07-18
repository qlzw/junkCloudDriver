package org.cloud.driver.Utils;

import org.cloud.driver.model.Common;

import java.io.*;

/**
 * @Classname FileUtils
 * @Description TODO
 * @Date 2020/5/28 21:30
 * @Created by 87454
 */
public class FileUtils {
    public static boolean isSameFile(File file1, File file2) {
        if (file1.length() != file2.length()) {
            return false;
        }
        try {
            InputStream is1 = new FileInputStream(file1);
            InputStream is2 = new FileInputStream(file2);
            byte[] bytes1 = new byte[is1.available()];
            byte[] bytes2 = new byte[is2.available()];
            is1.read(bytes1);
            is2.read(bytes2);
            if (MD5Util.md5(bytes1).equals(MD5Util.md5(bytes2))) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean isSameFile(FileInputStream is1, FileInputStream is2) {
        try {
            int len1 = is1.available();
            int len2 = is2.available();
            if (len1 != len2) {
                return false;
            }
            byte[] bytes1 = new byte[len1];
            byte[] bytes2 = new byte[len2];
            is1.read(bytes1);
            is2.read(bytes2);
            if (MD5Util.md5(bytes1).equals(MD5Util.md5(bytes2))) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String getFileMD5(InputStream is){
        try {
            int len = is.available();
            byte[] bytes = new byte[len];
            is.read(bytes);
            return MD5Util.md5(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSameFile(File file, InputStream is2) {
        try {
            InputStream is1 = new FileInputStream(file);
            int len1 = is1.available();
            int len2 = is2.available();
            if (len1 != len2) {
                return false;
            }
            byte[] bytes1 = new byte[len1];
            byte[] bytes2 = new byte[len2];
            is1.read(bytes1);
            is2.read(bytes2);
            if (MD5Util.md5(bytes1).equals(MD5Util.md5(bytes2))) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * 递归修改文件名（复制粘贴文件）
     * @param newFile 要写入的文件路径名
     * @param readFile 要读入的文件路径名
     * @param gen
     * @return
     */
    public static File rename(File newFile, File readFile, int gen) {
        if (!newFile.exists()) {
            System.out.println("同名文件存在，新建文件");
            return newFile;
        }
        if(isSameFile(newFile, readFile)) {
            System.out.println("相同文件已存在，不写入");
            return null;
        }
        String[] names = newFile.getName().split("\\.");
        String name = names[0] + "(" + gen + ")" + "." + names[1];
        newFile = new File(Common.WINDOWS_REAL_PATH + name);
        return rename(newFile, readFile,gen+1);
    }
    /**
     * 递归修改文件名（复制粘贴文件）
     * @param newFile 要写入的文件路径名
     * @param fis 已读入的文件流
     * @param gen
     * @return
     */
    public static File rename(File newFile, InputStream fis, int gen) {
        if (!newFile.exists()) {
            System.out.println("同名文件存在，新建文件");
            return newFile;
        }
        if(isSameFile(newFile, fis)) {
            System.out.println("相同文件已存在，不写入");
            return null;
        }
        String[] names = newFile.getName().split("\\.");
        String name = names[0] + "(" + gen + ")" + "." + names[1];
        newFile = new File(Common.WINDOWS_REAL_PATH + name);
        return rename(newFile, fis,gen+1);
    }

    public static void main(String[] args) {
        String realName = "haha1.png";
        String path = Common.WINDOWS_REAL_PATH + realName;
        File readfile = new File("D:/aa.png");
        File writefile = new File(path);
        System.out.println(isSameFile(readfile,writefile));

    }
}
