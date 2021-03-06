package com.utils.thirdutils;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.utils.toolutils.PropertiesUtil;
import com.utils.toolutils.ToolUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class QiNiuUtil {
    PropertiesUtil property = new PropertiesUtil();
    public static Auth dummyAuth = Auth.create(PropertiesUtil.AK, PropertiesUtil.SK);

    public static String getToken(String bucket) {
        return dummyAuth.uploadToken(bucket);
    }

    /**
     * 以字节的方式上传文件
     *
     * @param bucket  命名空间
     * @param key     文件名，可不传，不传则以hash值作为文件名
     * @param content 文件流
     */

    public static Map uploadString(String bucket, String key, String content) {
        Map map = new HashMap();
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            byte[] uploadBytes = content.getBytes("utf-8");
            String upToken = dummyAuth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                map.put("hash", putRet.hash);
                map.put("key", putRet.key);
                map.put("hashWithPrefix", PropertiesUtil.getDomain(bucket) + putRet.hash);
                map.put("keyWithPrefix", PropertiesUtil.getDomain(bucket) + putRet.key);
                return map;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    return null;
                }
            }
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        return null;
    }

    /**
     * 以文件的方式上传文件
     *
     * @param bucket 命名空间
     * @param key    文件名，可不传，不传则以hash值作为文件名
     * @param file   文件
     */
    public static Map uploadFile(String bucket, String key, File file) {
        Map map = new HashMap();
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            String upToken = dummyAuth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                map.put("hash", putRet.hash);
                map.put("key", putRet.key);
                map.put("hashWithPrefix", PropertiesUtil.getDomain(bucket) + putRet.hash);
                map.put("keyWithPrefix", PropertiesUtil.getDomain(bucket) + putRet.key);
                return map;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    return null;
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    /**
     * 以流的方式上传文件
     *
     * @param bucket 命名空间
     * @param key    文件名，可不传，不传则以hash值作为文件名
     * @param io     文件流
     */

    public static Map uploadStream(String bucket, String key, InputStream io) {
        Map map = new HashMap();
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            String upToken = dummyAuth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(io, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                map.put("hash", putRet.hash);
                map.put("key", putRet.key);
                map.put("hashWithPrefix", PropertiesUtil.getDomain(bucket) + putRet.hash);
                map.put("keyWithPrefix", PropertiesUtil.getDomain(bucket) + putRet.key);
                return map;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    return null;
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    /**
     * 根据文件名批量删除文件
     *
     * @param bucket  命名空间
     * @param keyList 文件名数组
     */
    public static void batchDelFile(String bucket, String[] keyList) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        BucketManager bucketManager = new BucketManager(dummyAuth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String keyId = keyList[i];
                System.out.print(keyId + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }


    /**
     * base64图片上传
     *
     * @param bucket
     * @param file64
     * @param key
     * @param fileSize
     * @throws Exception
     */

    public static Map<String, String> put64image(String bucket, String file64, String key, long fileSize) throws Exception {
        Map<String, String> map = new HashMap<>();
        String url = "http://upload-z2.qiniu.com/putb64/" + "-1" + "/key/" + UrlSafeBase64.encodeToString(key);
        file64 = file64.replaceFirst("data:image/jpeg;base64,", "");
        System.out.println(url);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        String uptoken = dummyAuth.uploadToken(bucket);
        System.out.println(uptoken);
        Request request = new Request.Builder().
                url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + uptoken)
                .post(rb).build();
        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            map.put("hash", key);
            map.put("key", key);
            map.put("hashWithPrefix", PropertiesUtil.getDomain(bucket) + key);
            map.put("keyWithPrefix", PropertiesUtil.getDomain(bucket) + key);
        }
        System.out.println(response);
        System.out.println(JSON.toJSONString(response));
        return map;
    }

    public static void main(String[] args) throws QiniuException {
//        System.out.println(getToken(BucketType.DOUPAI_TEST_AD.getCode() + ""));
//        System.out.println(getToken("11"));
//        uploadString("doupai-offical-banners", null, "test");
//        String[] key = {"FqlKj-XMsZumHEwIc9OR6YeYL7vT","Fn1iAdTkUNGQ50rKaJ1Gw1SXjkrq"};
//        batchDelFile("doupai-test-banners",key);
//        Map map =QiNiuUtil.uploadFile("doupai-offical-banners",null,new File("E:\\3.jpg"));
        String file64 = "data:image/jpeg;base64,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";
//        String file64="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";
        try {
            Map<String, String> m = put64image("doupai-test-user", file64, ToolUtil.getUUID(), -1);
            if (m.size() > 0) {
                System.out.println("map " + m.get("keyWithPrefix"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

