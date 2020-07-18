package org.cloud.driver.Utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private static final String SALT="chengwenjieshiwoerzi";

    private static String md5(String password){
        return DigestUtils.md5Hex(password);
    }

    public static String md5(byte[] bytes){
        return DigestUtils.md5Hex(bytes);
    }

    public static String md5WithSalt(String password){
        return md5(md5(password)+SALT);
    }
    public static void main(String[] args) {
        System.out.println(md5("12345678"));
        System.out.println(md5WithSalt("12345678"));

        System.out.println();


    }
}
