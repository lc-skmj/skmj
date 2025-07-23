package com.skmj.server.transcoding.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;

/**
 * minio文件上传工具类
 * @author: jeecg-boot
 */
@Slf4j
@Component
public class MinioUtil {
    
    @Value("${minio.minio_url}")
    private String minioUrl;
    
    @Value("${minio.minio_name}")
    private String minioName;
    
    @Value("${minio.minio_pass}")
    private String minioPass;
    
    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;
    
    @PostConstruct
    public void initMinio() {
        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(minioName, minioPass)
                    .build();
            log.info("MinIO客户端初始化成功，URL: {}, Bucket: {}", minioUrl, bucketName);
        } catch (Exception e) {
            log.error("MinIO客户端初始化失败", e);
            throw new RuntimeException("MinIO客户端初始化失败", e);
        }
    }

    public String getMinioUrl() {
        return minioUrl;
    }

    public String getBucketName() {
        return bucketName;
    }


    /**
     * 获取文件流
     * @param objectName 对象名称
     * @param fileUrl 保存目录
     * @param fileName 文件名
     * @return 下载后的文件路径
     */
    public String downloadFile(String objectName, String fileUrl, String fileName) {
        InputStream inputStream = null;
        String filePath = fileUrl + File.separator + IdUtil.getSnowflakeNextId() + "_." + fileName;
        try {
            GetObjectArgs objectArgs = GetObjectArgs.builder()
                    .object(objectName)
                    .bucket(bucketName)
                    .build();
            inputStream = minioClient.getObject(objectArgs);
            FileUtil.writeFromStream(inputStream, new File(filePath));
            log.info("文件下载成功，路径: {}", filePath);
        } catch (Exception e) {
            log.error("文件获取失败, objectName: {}, fileName: {}", objectName, fileName, e);
            filePath = null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.warn("关闭输入流失败", e);
                }
            }
        }
        return filePath;
    }

    /**
     * 删除文件
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     */
    public void removeObject(String bucketName, String objectName) {
        try {
            RemoveObjectArgs objectArgs = RemoveObjectArgs.builder()
                    .object(objectName)
                    .bucket(bucketName)
                    .build();
            minioClient.removeObject(objectArgs);
            log.info("文件删除成功, bucketName: {}, objectName: {}", bucketName, objectName);
        } catch (Exception e) {
            log.error("文件删除失败, bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw new RuntimeException("文件删除失败", e);
        }
    }


    /**
     * 上传文件到minio
     * @param stream 文件输入流
     * @param relativePath 文件名称加路径
     * @return 上传后的文件路径
     */
    public String upload(InputStream stream, String relativePath) {
        try {
            // 检查存储桶是否存在
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            
            if (!bucketExists) {
                // 创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("创建新的存储桶: {}", bucketName);
            } else {
                log.debug("存储桶已存在: {}", bucketName);
            }
            
            // 上传文件
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .object(relativePath)
                    .bucket(bucketName)
                    .contentType("application/octet-stream")
                    .stream(stream, stream.available(), -1)
                    .build();
            
            minioClient.putObject(objectArgs);
            log.info("文件上传成功, bucketName: {}, relativePath: {}", bucketName, relativePath);
            
            return relativePath;
            
        } catch (Exception e) {
            log.error("文件上传失败, bucketName: {}, relativePath: {}", bucketName, relativePath, e);
            throw new RuntimeException("文件上传失败", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    log.warn("关闭输入流失败", e);
                }
            }
        }
    }


}
