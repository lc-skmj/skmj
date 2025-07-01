package com.skmj.server.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.util.PoitlIOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * POITL模块演示类
 * 展示如何使用WordUtil工具类进行Word文档操作
 */
public class Demo {
    public static void main(String[] args) {
        // 创建Word文档示例
        String filePath = "example.docx";
        String content = "这是一个POI-TL文档处理示例。\n" + 
                       "POI-TL是一个强大的Java Word模板引擎。\n" + 
                       "它可以用于创建和修改Word文档。";
        WordUtil.createWordDocument(filePath, content);
        System.out.println("文档创建完成。");
        
        // 读取Word文档示例
        String readContent = WordUtil.readWordDocument(filePath);
        System.out.println("文档内容：\n" + readContent);
        
        // 修改Word文档示例
        String newFilePath = "modified_example.docx";
        String oldText = "POI-TL";
        String newText = "Apache POI";
        WordUtil.replaceInWordDocument(filePath, newFilePath, oldText, newText);
        System.out.println("文档修改完成。");
        
        // 验证修改结果
        String modifiedContent = WordUtil.readWordDocument(newFilePath);
        System.out.println("修改后的文档内容：\n" + modifiedContent);
    }
}
