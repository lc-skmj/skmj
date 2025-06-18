package com.skmj.server.aimodel;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * AI模型控制器
 * 提供REST API接口
 */
@RestController
@RequestMapping("/api/ai-model")
public class AIModelController {
    private final AIModelService aiModelService;
    
    public AIModelController(AIModelService aiModelService) {
        this.aiModelService = aiModelService;
    }
    
    /**
     * 调用AI模型进行预测
     * @param inputParams 输入参数
     * @return 预测结果
     */
    @PostMapping("/predict")
    public Map<String, Object> predict(@RequestBody Map<String, Object> inputParams) {
        return aiModelService.predict(inputParams);
    }
    
    /**
     * 获取AI模型版本信息
     * @return 版本信息
     */
    @GetMapping("/version")
    public String getModelVersion() {
        return aiModelService.getModelVersion();
    }

    @PostMapping("/generate-text")
    public String generateText(@RequestParam String prompt, @RequestParam int maxTokens) {
        return aiModelService.generateText(prompt, maxTokens);
    }

    @GetMapping("/stream-generate-text")
    public void streamGenerateText(@RequestParam String prompt, @RequestParam int maxTokens, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        
        aiModelService.streamGenerateText(prompt, maxTokens, textChunk -> {
            try {
                response.getWriter().write(textChunk);
                response.getWriter().flush();
            } catch (IOException e) {
                // 处理异常
            }
        });
    }

    @PostMapping("/async-generate-text")
    public void asyncGenerateText(@RequestParam String prompt, @RequestParam int maxTokens, @RequestBody AsyncRequest request, HttpServletRequest httpServletRequest) {
        String apiKey = httpServletRequest.getHeader("X-API-KEY");
        String clientIp = httpServletRequest.getRemoteAddr();
        
        if (!validateApiKey(apiKey)) {
            throw new IllegalArgumentException("Invalid API Key");
        }
        
        if (!isRequestAllowed(clientIp)) {
            throw new IllegalArgumentException("Too Many Requests");
        }
        
        aiModelService.asyncGenerateText(prompt, maxTokens, new AIModelService.TextCallback() {
            @Override
            public void onComplete(String result) {
                // 这里可以实现回调逻辑，比如发送WebSocket消息或存储结果
            }
        });
    }
    
    // 用于异步请求的DTO
    private static class AsyncRequest {
        // 可以根据需要添加回调URL等参数
    }

    @PostMapping("/generate-embedding")
    public double[] generateEmbedding(@RequestParam String text) {
        return aiModelService.generateEmbedding(text);
    }
    
    // 简单的API密钥验证（实际应使用更安全的方式）
    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String VALID_API_KEY = "sk_1234567890abcdef"; // 示例密钥，实际应从安全存储获取
    
    // 简单的请求限流器（实际应使用Guava RateLimiter或Redis分布式限流）
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private final Map<String, AtomicInteger> requestCounters = new ConcurrentHashMap<>();
    
    /**
     * 验证API密钥
     * @param apiKey 提供的API密钥
     * @return 是否有效
     */
    private boolean validateApiKey(String apiKey) {
        return VALID_API_KEY.equals(apiKey);
    }
    
    /**
     * 检查是否允许请求（简单IP限流）
     * @param clientIp 客户端IP
     * @return 是否允许
     */
    private boolean isRequestAllowed(String clientIp) {
        AtomicInteger counter = requestCounters.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
        int count = counter.incrementAndGet();
        
        // 重置计数器（实际应使用滑动窗口算法）
        // 这里只是简单的示例，实际应有更完善的限流机制
        if (count > MAX_REQUESTS_PER_MINUTE) {
            return false;
        }
        
        // 启动一个后台线程定期清理计数器
        new Thread(() -> {
            try {
                Thread.sleep(60000); // 1分钟后重置
                requestCounters.remove(clientIp, counter); // 只有当值不变时才移除
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        return true;
    }
    
    // 在所有需要鉴权和限流的方法中添加以下代码：
    // String apiKey = request.getHeader(API_KEY_HEADER);
    // if (!validateApiKey(apiKey)) {
    //     response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
    //     return;
    // }
    // 
    // String clientIp = request.getRemoteAddr();
    // if (!isRequestAllowed(clientIp)) {
    //     response.sendError(HttpServletResponse.SC_TOO_MANY_REQUESTS, "Too Many Requests");
    //     return;
    // }
}