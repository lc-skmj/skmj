package com.skmj.server.xxljob.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * 示例定时任务处理器
 */
@Component
public class SampleJobHandler {
    
    @XxlJob("sampleJobHandler")
    public void sampleJobHandler() throws Exception {

        // 业务逻辑结束
    }
}