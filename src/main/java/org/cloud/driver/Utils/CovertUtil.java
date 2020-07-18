package org.cloud.driver.Utils;

/**
 * @Classname CovertUtil
 * @Description TODO
 * @Date 2020/5/29 11:21
 * @Created by 87454
 */
public class CovertUtil {
    private static final long SIZE_KB = 1024;
    //1024*1024
    private static final long SIZE_MB = 1048576;
    //1024*1024*1024
    private static final long SIZE_GB = 1073741824;

    /**
     * long类型size转化为string类型的size,保留一位小数
     * @param size
     * @return
     */
    public static String size2String(long size){
        if(size > SIZE_GB){
            if((size%SIZE_GB)/(SIZE_GB/10) > 0)
                return size/SIZE_GB+"."+(size%SIZE_GB)/(SIZE_GB/10)+"G";
            else
                return size/SIZE_GB+"G";
        }else if(size > SIZE_MB){
            if((size%SIZE_MB)/(SIZE_MB/10) > 0)
                return size/SIZE_MB+"."+ (size%SIZE_MB)/(SIZE_MB/10)+"M";
            else
                return size/SIZE_MB+"M";
        }else if(size > SIZE_KB) {
            if((size%SIZE_KB)/(SIZE_KB/10) > 0)
                return size/SIZE_KB+"."+(size%SIZE_KB)/(SIZE_KB/10)+"K";
            else
                return size/SIZE_KB+"K";
        }else{
            return size+"B";
        }
    }
    public static long string2Size(String size){
        char unit = size.charAt(size.length()-1);
        long len = Long.parseLong(size.substring(0,size.length()-1));
        switch (unit){
            case 'G':
                len*=SIZE_GB;
                break;
            case 'M':
                len*=SIZE_MB;
                break;
            case 'K':
                len*=SIZE_KB;
                break;
            default:
                len*=1;
                break;
        }
        return len;
    }
}
