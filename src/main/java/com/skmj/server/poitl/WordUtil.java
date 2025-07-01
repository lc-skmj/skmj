package com.skmj.server.poitl;
// 新增Word工具类

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Word文档处理工具类
 * 提供创建、读取、修改Word文档的常用方法
 */
public class WordUtil {
    /**
     * 创建新的Word文档并写入内容
     * @param filePath 创建的文件路径
     * @param content 要写入的内容
     */
    public static void createWordDocument(String filePath, String content) {
        try {
            XWPFDocument document = new XWPFDocument();
            
            // 创建段落
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(content);
            
            // 保存文档
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 读取Word文档内容
     * @param filePath 文档路径
     * @return 文档内容字符串
     */
    public static String readWordDocument(String filePath) {
        StringBuilder content = new StringBuilder();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            
            // 读取段落内容
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                content.append(paragraph.getText()).append("\n");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return content.toString();
    }
    
    /**
     * 在Word文档中替换指定文本
     * @param inputPath 输入文档路径
     * @param outputPath 输出文档路径
     * @param oldText 要替换的旧文本
     * @param newText 新文本
     */
    public static void replaceInWordDocument(String inputPath, String outputPath, String oldText, String newText) {
        try (FileInputStream fis = new FileInputStream(inputPath);
             XWPFDocument document = new XWPFDocument(fis)) {
            
            // 替换段落中的文本
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text.contains(oldText)) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    if (runs != null && !runs.isEmpty()) {
                        // 清除原有内容
                        runs.get(0).setText(newText, 0);
                        for (int i = 1; i < runs.size(); i++) {
                            runs.get(i).setText("", 0);
                        }
                    }
                }
            }
            
            // 保存修改后的文档
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                document.write(out);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}