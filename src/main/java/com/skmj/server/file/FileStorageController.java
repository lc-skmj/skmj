package com.skmj.server.file;



import com.skmj.server.util.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/file")
public class FileStorageController {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("upload/")
                .thumbnail(th -> th.size(200, 200))
                .upload();
        return Result.success(fileInfo);
    }
    
    /**
     * 文件删除
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam("url") String url) {
        boolean result = fileStorageService.delete(url);
        return Result.success(result);
    }
    
    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(@RequestParam("url") String url, HttpServletResponse response) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            fileStorageService.download(url).outputStream(out);
            response.getOutputStream().write(out.toByteArray());
            out.close();
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}