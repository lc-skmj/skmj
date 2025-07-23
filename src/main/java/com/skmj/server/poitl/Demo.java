package com.skmj.server.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.util.PoitlIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * POITL模块演示类
 * 展示如何使用WordUtil工具类进行Word文档操作
 */
public class Demo {
    
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);
    
    public static void main(String[] args) {
        // 创建Word文档示例
        String filePath = "example.docx";
        String content = "这是一个POI-TL文档处理示例。\n" + 
                       "POI-TL是一个强大的Java Word模板引擎。\n" + 
                       "它可以用于创建和修改Word文档。";
        WordUtil.createWordDocument(filePath, content);
        logger.info("文档创建完成。");
        
        // 读取Word文档示例
        String readContent = WordUtil.readWordDocument(filePath);
        logger.info("文档内容：\n{}", readContent);
        
        // 修改Word文档示例
        String newFilePath = "modified_example.docx";
        String oldText = "POI-TL";
        String newText = "Apache POI";
        WordUtil.replaceInWordDocument(filePath, newFilePath, oldText, newText);
        logger.info("文档修改完成。");
        
        // 验证修改结果
        String modifiedContent = WordUtil.readWordDocument(newFilePath);
        logger.info("修改后的文档内容：\n{}", modifiedContent);
    }
}
