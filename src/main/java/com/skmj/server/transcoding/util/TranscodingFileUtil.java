package com.skmj.server.transcoding.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.skmj.server.transcoding.entity.MyFile;
import com.skmj.server.transcoding.entity.TranscodingFile;
import com.skmj.server.transcoding.service.MyFileService;
import com.skmj.server.transcoding.service.TranscodingFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lc
 */
@Slf4j
@Component
public class TranscodingFileUtil {
    @Autowired
    private TranscodingFileService transcodingFileService;
    @Autowired
    private MyFileService myFileService;
    @Value("${ffmpeg.path}")
    private String ffmpegPath;
    @Value("${ffmpeg.outputDir}")
    private String outputDir;
    @Value("${minio.minio_url}")
    private String minoUrl;

    @Value("${ffmpeg.tmpDir}")
    private String tmpDir;
    @Autowired
    private WordToImgUtil wordToImgUtil;
    @Autowired
    private PptToImgUtil pptToImgUtil;

    public void transcodingFile(List<MyFile> list) {
        for (MyFile resource : list) {
            if (resource.getTranscodingStatus() !=3&& resource.getTranscodingStatus() !=0) {
                log.info("当前转码文件状态为：{}，跳过转码流程", resource.getTranscodingStatus());
                continue;
            }
            //下载文件到临时目录
            log.info("开始下载文件");
            String tmpDirFile = tmpDir+File.separator+ resource.getId();
            String output = outputDir+File.separator+resource.getId();
            File file = new File(tmpDirFile);
            if (!file.exists()) {
                log.info("{}目录不存在，创建{}", tmpDirFile, tmpDirFile);
                boolean mkdir = file.mkdir();
                log.info("{}目录创建：{}", tmpDirFile, mkdir);
            }
            File file1 = new File(output);
            if (!file1.exists()) {
                log.info("{}目录不存在，创建{}", output, output);
                boolean mkdir = file1.mkdir();
                log.info("{}目录创建：{}", tmpDirFile, mkdir);
            }
            long l1 = System.currentTimeMillis();
            String replace = resource.getVideos().replaceAll(minoUrl + "/"+MinioUtil.getBucketName(), "");
            String inputUrl = MinioUtil.downloadFile(replace, tmpDirFile, resource.getExtension());
            long l2 = System.currentTimeMillis() - l1;
            log.info("下载文件内到：{},用时：{}秒", inputUrl, l2 / 1000);
            if (inputUrl == null) {
                log.info("文件为空，跳过");
                continue;
            }
            ThreadUtil.execAsync(()->{
            boolean b = false;
            //更新 源文件状态为已转码
            resource.setTranscodingStatus(2);
            boolean b1 = myFileService.updateById(resource);
            log.info("更新资源为转码中状态：{}", b1);

            log.info("转码视频输出目录：{}",output);
            if ("VIDEO".equalsIgnoreCase(resource.getType())) {
                b = CommandVideoUtil.executeCommandVideo(ffmpegPath, output, inputUrl);
            }
            if ("DOCUMENT".equals(resource.getType())) {
                if ("pdf".equalsIgnoreCase(resource.getExtension())) {
                    b = PdfToImgUtil.executeCommandPdf(output, inputUrl);
                }
                if ("docx".equalsIgnoreCase(resource.getExtension())|| "doc".equalsIgnoreCase(resource.getExtension())) {
                    b = wordToImgUtil.executeCommandWord(output, inputUrl);
                }
                if ("ppt".equalsIgnoreCase(resource.getExtension())) {
                    b = pptToImgUtil.executeCommandPpt(output, inputUrl);
                }
            }
            if (b) {
                log.info("{}，转码成功", resource.getId());
                // 获取转码后得文件，上传minio,保存数据库
                File[] ls = FileUtil.ls(output);
                List<Boolean> result = new ArrayList<>();
                List<TranscodingFile> saveResult = new ArrayList<>();
                for (File l : ls) {
                    if (l.isDirectory()) {
                        continue;
                    }
                    try (InputStream inputStream = new FileInputStream(l)) {
                        String upload = MinioUtil.upload(inputStream, "transcoding/" + l.getName());
                        TranscodingFile t = new TranscodingFile();
                        t.setRid(resource.getId());
                        t.setFileUrl(upload);
                        saveResult.add(t);
                        result.add(true);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        result.add(false);
                    }
                }
                if (result.contains(false)) {
                    log.info("有上传失败的文件，不进行入库操作");
                    return;
                }
                for (int i = 0; i < saveResult.size(); i++) {
                    TranscodingFile transcodingFile = saveResult.get(i);
                    if (transcodingFile.getFileUrl().contains("m3u8")) {
                        // 移除该对象
                        TranscodingFile m3u8File = saveResult.remove(i);
                        // 将该对象插入到列表的开头
                        saveResult.add(0, m3u8File);
                        // 退出循环，因为我们只需要移动第一个找到的 "m3u8" 对象
                        break;
                    }
                }
                boolean save = transcodingFileService.saveBatch(saveResult);
                log.info("保存转码文件结果：{}", save ? "成功" : "失败");
                //删除临时文件
                boolean del = FileUtil.del(output);
                log.info("删除临时输出目录：{}", del);
                boolean del1 = FileUtil.del(tmpDirFile);
                log.info("删除临时下载目录：{}", del1);
                //更新 源文件状态为已转码
                resource.setTranscodingStatus(1);
                boolean b2 = myFileService.updateById(resource);
                log.info("更新资源为已转码状态：{}", b2);
            } else {
                resource.setTranscodingStatus(3);
                boolean b3 = myFileService.updateById(resource);
                log.info("更新资源为转码失败状态：{}", b3);
                log.info("{}，转码失败", resource.getId());
                boolean del1 = FileUtil.del(tmpDirFile);
                log.info("删除临时下载目录：{}", del1);
            }
            });
        }
    }
}
