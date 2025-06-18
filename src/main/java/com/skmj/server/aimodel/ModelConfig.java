package com.skmj.server.aimodel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 模型配置参数
 * 用于管理大模型的各种参数
 */
@Component
@ConfigurationProperties(prefix = "ai.model")
public class ModelConfig {
    private String modelName;
    private int defaultMaxTokens;
    private double temperature;
    private int maxRetries;
    private long timeoutMillis;
    
    // Getters and Setters
    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public int getDefaultMaxTokens() {
        return defaultMaxTokens;
    }
    
    public void setDefaultMaxTokens(int defaultMaxTokens) {
        this.defaultMaxTokens = defaultMaxTokens;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public long getTimeoutMillis() {
        return timeoutMillis;
    }
    
    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
}