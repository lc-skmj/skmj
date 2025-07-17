package com.skmj.server.xxljob;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-JOB 配置类
 */
@Configuration
public class XXLJobConfig {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        return new XxlJobSpringExecutor();
    }
}