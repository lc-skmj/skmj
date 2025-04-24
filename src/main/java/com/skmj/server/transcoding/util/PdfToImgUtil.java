package com.skmj.server.transcoding.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author lc
 */
@Slf4j
public class PdfToImgUtil {

    public static boolean executeCommandPdf(String outputDir, String inputUrl){
        // PDF文件路径
        try {
            File pdfFile = new File(inputUrl);
            PDDocument document = PDDocument.load(pdfFile);

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            String snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr();
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                if (!new File(outputDir).exists()){
                    FileUtil.mkdir(outputDir);
                }
                ImageIO.write(bim, "JPEG", new File(outputDir+File.separator+snowflakeNextIdStr+"_" + (page + 1) + ".jpg"));
          log.info("进度：{}",(page+1/document.getNumberOfPages())*100);
            }
            document.close();
            log.info("pdf转图片成功");
            return true;
        }catch (Exception e){
        log.info("PDF转图片异常",e);
        }
        return false;
    }

}
