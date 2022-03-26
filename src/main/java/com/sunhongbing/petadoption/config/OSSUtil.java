package com.sunhongbing.petadoption.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * @className: OSSUtil
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-24 18:19
 */

public class OSSUtil {

    private final String endpoint = OSSProperties.ALIYUN_ENDPOINT;

    private final String accessKeyId = OSSProperties.ALIYUN_ACCESS_ID;

    private final String accessKeySecret = OSSProperties.ALIYUN_ACCESS_KEY;

    private final String bucketName = OSSProperties.ALIYUN_BUCKET;

    private final String FOLDER = OSSProperties.ALIYUN_DIR;

    /**
     * 删除单个图片
     *
     * @param url
     */
    public void deleteImg(String url) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        url = FOLDER + url;
        ossClient.deleteObject(bucketName, url);
        System.out.println("删除 " + url + " 成功");
        ossClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public String uploadImg2Oss(MultipartFile file, String fileName) throws IOException {
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, fileName);
            String url = "https://" + bucketName + "." + endpoint + "/" + FOLDER +"/"+ fileName;
            return url;
        } catch (Exception e) {
            throw new IOException("图片上传失败");
        }
    }

    /**
     * 上传到OSS服务器 如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {

        String ret = "";
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, FOLDER +"/"+ fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
            System.out.println(e.getMessage());
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     */
    public static String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        // url = "https://" + bucketName + ".oss-cn-beijing.aliyuncs.com/" + bucketName+"/"+ key;
        if (url != null) {
            String host = "https://" + url.getHost() + url.getPath();
            return host;
        }
        return "";
    }

}
