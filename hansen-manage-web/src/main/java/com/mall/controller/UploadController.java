package com.mall.controller;

import com.mall.core.page.JsonResult;
import com.mall.util.PropertiesUtil;
import com.mall.util.QiNiuUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by summer on 2016-12-12:12:15;
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

    /***
     * 获取上传文件的token
     */
    @RequestMapping("/getToken")
    @ResponseBody
    public JsonResult pageSysManUser(HttpServletRequest request, HttpServletResponse response, String folderName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", QiNiuUtil.getToken(folderName));
        if (StringUtils.isEmpty(folderName)){
            map.put("domain", PropertiesUtil.getDomain("prefix"));
        }
        else {
            map.put("domain", PropertiesUtil.getDomain(folderName));
        }
//        map.put("domain", "http://www.baidu.com");
        return new JsonResult(map);
    }

/*    private static Map<String, String> contentTypeMap = new HashMap<>();

    static {
        contentTypeMap.put(".jpg", "image/jpg");
        contentTypeMap.put(".gif", "image/gif");
        contentTypeMap.put(".png", "image/png");
        contentTypeMap.put(".jpeg", "image/jpeg");
        contentTypeMap.put(".bmp", "application/x-bmp");
    }

    @Value("${baseDirPath}")
    String baseDirPath;
    @Value("${upLoadDomain}")
    String upLoadDomain;


    @RequestMapping(value = "/image/upload", method ={RequestMethod.POST,RequestMethod.OPTIONS})
    @ResponseBody
    public JsonResult uploadImg( MultipartFile file, HttpServletRequest request){
        if (request.getMethod().toString().equals("OPTIONS")){
            return success();
        }
        String fileName=file.getOriginalFilename();
        if (!isPicture(fileName)){
            return fail("只允许上传图片");
        }
        final String imageType =getFileType(fileName);
        String realPictureName = UUID.randomUUID().toString().replace("-", "").toUpperCase() + imageType;
        // 文件夹命名规则为年月的字符串形式,如:201410
        final String folder = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM")+"/jpg";
        // 上传到本地
        String fullPath=uploadService.uploadPictureToLocal(file, baseDirPath, "/" + folder, realPictureName);
        String path=folder+"/"+realPictureName;
        return success(path);
    }

    public static boolean isPicture(String name) {
        name = name.toLowerCase();
        for (String type : contentTypeMap.keySet()) {
            if (name.toLowerCase().endsWith(type))
                return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/uploadDomain")
    public JsonResult getUploadDomain(){
        return success(upLoadDomain);
    }
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getImageContentType(String name) {
        String fileType = getFileType(name).toLowerCase();
        return contentTypeMap.get(fileType);
    }*/
}
