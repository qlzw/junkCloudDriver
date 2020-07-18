package org.cloud.driver.Utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.FutureTask;

public class UUIDUtil {
    private static final String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /*
    生成唯一主键
    格式：时间+随机数
     */
    public static synchronized long getUniqueKey(){
        Random random = new Random();
        Integer number = random.nextInt(90000) + 100000;
        System.out.println(number);
        return Long.valueOf(System.currentTimeMillis()+number);
    }

    /**
     * 生成唯一32位id
     * @return String
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 生成length位验证码
     * @param length
     * @return
     */
    public static String getCode(int length){
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();
        for(int i = 0; i < length; i++){
            stringBuilder.append(str.charAt(random.nextInt(str.length())));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        /**
        final int THREAD_NUM = 10;
        for(int i = 0; i  < THREAD_NUM; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(getCode(6));
                }
            }).start();
        }
         **/
        String emailAddress = "874549232@qq.com";
        String code = UUIDUtil.getCode(6);
        FutureTask<Boolean> futureTask = new FutureTask<>(new EmailUtil(emailAddress, code));
        Thread t1 = new Thread(futureTask);
        t1.start();
    }
}
