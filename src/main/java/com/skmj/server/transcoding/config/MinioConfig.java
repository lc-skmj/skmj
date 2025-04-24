package com.skmj.server.transcoding.config;

import com.skmj.server.transcoding.util.MinioUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Minio文件上传配置文件
 * @author: jeecg-boot
 */
@Slf4j
@Configuration
public class MinioConfig {
    @Value(value = "${minio.minio_url}")
    private String minioUrl;
    @Value(value = "${minio.minio_name}")
    private String minioName;
    @Value(value = "${minio.minio_pass}")
    private String minioPass;
    @Value(value = "${minio.bucketName}")
    private String bucketName;

    @PostConstruct
    public void initMinio(){
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setMinioName(minioName);
        MinioUtil.setMinioPass(minioPass);
        MinioUtil.setBucketName(bucketName);
    }

}
