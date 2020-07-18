package org.cloud.driver.Utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname TypeCovertUtils
 * @Description TODO
 * @Date 2020/6/4 20:46
 * @Created by 87454
 */
public class TypeCovertUtil {
    static Map<String, String> typeMap;
    static{
        typeMap = new HashMap<>();
        //图片
        typeMap.put("jpg","image");
        typeMap.put("jpeg","image");
        typeMap.put("png","image");
        typeMap.put("gif","image");
        //视频
        typeMap.put("avi","video");
        typeMap.put("mov","video");
        typeMap.put("rmvb","video");
        typeMap.put("rm","video");
        typeMap.put("flv","video");
        typeMap.put("mp4","video");
        //文档
        typeMap.put("doc","document");
        typeMap.put("docx","document");
        typeMap.put("ppt","document");
        typeMap.put("pptx","document");
        typeMap.put("xls","document");
        typeMap.put("xlsx","document");
        typeMap.put("pdf","document");
        typeMap.put("txt","document");
        //压缩
        typeMap.put("rar","zip");
        typeMap.put("zip","zip");
        typeMap.put("7z","zip");
        //种子
        typeMap.put("torrent","torrent");
    }

    /**
     * 根据后缀名获取文件类型
      * @param suffix
     * @return
     */
    public static String getType(String suffix){
        return typeMap.containsKey(suffix)?typeMap.get(suffix):"other";
    }
}
