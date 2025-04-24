package com.skmj.server.transcoding.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author lc
 */
@Slf4j
@Service
public class PptToImgUtil {
    @Autowired
    private DocumentConverter documentConverter;

    public boolean executeCommandPpt(String outputDir, String inputUrl) {
        // Word文档路径
        File inputFile = new File(inputUrl);
        // 输出图片路径
        //先转换为pdf
        File pdfFile = new File(outputDir + File.separator + "tmp" + File.separator + "tmp.pdf");
        try {
            documentConverter.convert(inputFile)
                    .as(DefaultDocumentFormatRegistry.PPTX)
                    .to(pdfFile).as(DefaultDocumentFormatRegistry.PDF).execute();

            while (!FileUtil.isEmpty(pdfFile)) {
                boolean b = PdfToImgUtil.executeCommandPdf(outputDir, pdfFile.getPath());
                if (b) {
                    //删除临时pdf
                    FileUtil.del(pdfFile);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("word转图片异常", e);
        }
        return false;
    }

}
