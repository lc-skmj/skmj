package com.skmj.server.aimodel;

import java.util.Map;

/**
 * AI模型服务接口
 * 定义基础的AI调用方法
 */
public interface AIModelService {
    /**
     * 调用AI模型进行预测
     * @param inputParams 输入参数
     * @return 预测结果
     */
    Map<String, Object> predict(Map<String, Object> inputParams);
    
    /**
     * 调用大模型进行文本生成
     * @param prompt 输入提示
     * @param maxTokens 最大生成token数
     * @return 生成的文本结果
     */
    String generateText(String prompt, int maxTokens);
    
    /**
     * 流式生成文本（Server-Sent Events）
     * @param prompt 输入提示
     * @param maxTokens 最大生成token数
     * @param consumer 文本块消费函数
     */
    void streamGenerateText(String prompt, int maxTokens, TextConsumer consumer);
    
    /**
     * 异步生成文本
     * @param prompt 输入提示
     * @param maxTokens 最大生成token数
     * @param callback 异步回调函数
     */
    void asyncGenerateText(String prompt, int maxTokens, TextCallback callback);
    
    /**
     * 调用大模型进行嵌入向量生成
     * @param text 输入文本
     * @return 文本的嵌入向量
     */
    double[] generateEmbedding(String text);
    
    /**
     * 获取AI模型版本信息
     * @return 版本信息
     */
    String getModelVersion();
    
    @FunctionalInterface
    interface TextConsumer {
        void accept(String textChunk);
    }
    
    @FunctionalInterface
    interface TextCallback {
        void onComplete(String result);
        default void onError(Throwable error) {
            // 默认空实现
        }
    }
}