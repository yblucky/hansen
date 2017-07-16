package com.api.util;


import com.qiniu.common.QiniuException;
import com.qiniu.util.Auth;

public class QiNiuUtil {
    //PropertiesUtil property=new PropertiesUtil();
    public static Auth dummyAuth = Auth.create(PropertiesUtil.AK, PropertiesUtil.SK);
    //private static Auth dummyAuth = Auth.create("Nbr6AunCRxYgHjeGR8O38wv6v-zASUdGwDoWElsX","Rr0-lXEOAmpwFnPUD80TrZfcXFbzF8n8eVs1eBGx");
//	/** 公有资源下载 **/
//
//	/** 获取空间名列表 **/
//	private static BucketManager bucketManager = new BucketManager(dummyAuth);
//	/** 数据处理 **/
//	private static OperationManager operater = new OperationManager(dummyAuth);
//
//
//	public static String[] getBuckets() throws QiniuException {
//		String[] buckets = bucketManager.buckets();
//		return buckets;
//	}
//
//	/**
//	 * 根据前缀获得空间文件列表批量获取文件列表
//	 *
//	 * @param bucket
//	 *            空间名
//	 * @param prefix
//	 *            文件名前缀
//	 * @param limit
//	 *            每次迭代的长度限制，最大1000，推荐值 100
//	 * @param delimiter
//	 *            指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
//	 * @return FileInfo迭代器
//	 */
//
//	public  static FileInfo[] getFileInfoByFilePrefix(String bucket, String prefix, int limit, String delimiter) {
//		FileInfo[] items = null;
//		BucketManager.FileListIterator it = bucketManager.createFileListIterator(bucket, prefix, 100, null);
//		while (it.hasNext()) {
//			items = it.next();
//		}
//		return items;
//	}
//
//	/**
//	 * 查看文件属性
//	 *
//	 * @param bucket
//	 *            空间名
//	 * @param key
//	 *            文件名
//	 *
//	 * @throws QiniuException
//	 **/
//	public static FileInfo getFileInfoByKey(String bucket, String key) throws QiniuException {
//		FileInfo fileInfo = bucketManager.stat(bucket, key);
//		return fileInfo;
//	}
//
//	/**
//	 * 复制文件
//	 *
//	 * @param bucket
//	 *            被复制的空间名
//	 * @param key
//	 *            被复制的文件名
//	 * @param targetBucket
//	 *            目的的空间名
//	 * @param targetKey
//	 *            复制后的文件名
//	 *
//	 **/
//	public static Boolean copyFileToNewBuket(String bucket, String key, String targetBucket, String targetKey) {
//		try {
//			bucketManager.copy(bucket, key, targetBucket, targetKey);
//		} catch (QiniuException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 重命名文件
//	 *
//	 * @param bucket
//	 *            被重命名的空间名
//	 * @param key
//	 *            被重命名的文件名
//	 * @param targetKey
//	 *            重命名后的文件名
//	 *
//	 **/
//	public static Boolean renameFile(String bucket, String key, String renameKey) {
//		try {
//			bucketManager.rename(bucket, key, renameKey);
//		} catch (QiniuException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 移动文件
//	 *
//	 * @param bucket
//	 *            被移动的空间名
//	 * @param key
//	 *            被移动的文件名
//	 * @param targetBucket
//	 *            移动的目的空间名
//	 * @param targetKey
//	 *            移动后的文件名
//	 *
//	 **/
//	public static Boolean moveFileToNewBuket(String bucket, String key, String targetBucket, String targetKey) {
//		try {
//			bucketManager.move(bucket, key, targetBucket, targetKey);
//		} catch (QiniuException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 删除文件
//	 *
//	 * @param bucket
//	 *            被删除文件的空间名
//	 * @param key
//	 *            被删除文件的文件名
//	 *
//	 **/
//	public  static Boolean deleteFile(String bucket, String key) {
//		try {
//			bucketManager.delete(bucket, key);
//		} catch (QiniuException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/** 批量操作 **/
//	public static void batchFileInfo() {
//	}
//
//	/** 抓取资源 **/
//
//	/** 更新镜像资源 **/
//	/**
//	 * 数据处理接口 针对大图片(大于 20M)、音视频等处理。 在上传策略中(设置
//	 * persistentOps、persistentNotifyUrl、persistentPipeline参数)可指定上传成功后，生成一个异步任务，
//	 * 后台执行。 或 触发在线文件生成异步转码任务
//	 *
//	 * @param bucket
//	 * @param key
//	 * @param notifyURL
//	 * @param pipeline
//	 * @param force
//	 *
//	 **/
//
//	public static String dealWithData(String bucket, String key, String notifyURL, String pipeline, boolean force)
//			throws QiniuException {
//		StringMap params = new StringMap().putNotEmpty("notifyURL", notifyURL).putWhen("force", 1, force)
//				.putNotEmpty("pipeline", pipeline);
//		String fops = "avthumb/mp4/vcodec/libx264/acodec/libfaac/stripmeta/1";
//		fops += "|saveas/" + UrlSafeBase64.encodeToString(Config.accessKey + ":" + Config.secretKey);
//
//		try {
//			// 针对指定空间的文件触发 pfop 操作
//			String id = operater.pfop(bucket, key, fops, params);
//			// 可通过下列地址查看处理状态信息。
//			// 实际项目中设置 notifyURL，接受通知。通知内容和处理完成后的查看信息一致。
//			// String url = "http://api.qiniu.com/status/get/prefop?id=" + id;
//			return id;
//		} catch (QiniuException e) {
//			Response r = e.response;
//		}
//		return "";
//	}
//
//	public static void queryProcessById(String id) {
//		String url = "";
//		// TODD处理查询的接口
//	}

    public static String getToken(String folderName) {
        return dummyAuth.uploadToken(folderName);
    }

//    public static boolean deleteFile(String folderName, String urls) throws QiniuException {
//        BucketManager bucketManager = new BucketManager(dummyAuth);
//        String[] urlArray = urls.split(";");
//        for (int i = 0; i < urlArray.length; i++) {
//            String url = urlArray[i].substring(urlArray[i].lastIndexOf("/") + 1, urlArray[i].length());
//            bucketManager.delete(folderName, url);
//        }
//        return true;
//    }

    private static Object PropertiesUtil() {
        // TODO Auto-generated method stub
        return null;
    }

    //Uptoken
    public static void main(String[] args) throws QiniuException {
        //System.out.println(getToken(BucketType.MALL.getCode()));
        //System.out.println(getToken(""));
    }
}
