package com.skmj.server.xxlmq.config;

import com.xxl.mq.client.factory.impl.XxlMqSpringClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * mq配置
 * @author lc
 */
@Component
public class MqConfig {
    @Value("${xxl-mq.address}")
    private String address;
    @Value("${xxl-mq.token}")
    private String token;

    @Bean
    public XxlMqSpringClientFactory getXxlMqConsumer(){
        XxlMqSpringClientFactory xxlMqSpringClientFactory = new XxlMqSpringClientFactory();
        xxlMqSpringClientFactory.setAdminAddress(address);
        xxlMqSpringClientFactory.setAccessToken(token);
        return xxlMqSpringClientFactory;
    }
}
