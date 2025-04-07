package com.skmj.xxlmq.config;

import com.xxl.mq.client.factory.impl.XxlMqSpringClientFactory;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

/**
 * mq配置
 * @author lc
 */
@Component
public class MqConfig {
    @Inject("${xxl-mq.address}")
    private String address;
    @Inject("${xxl-mq.token}")
    private String token;

    @Bean
    public XxlMqSpringClientFactory getXxlMqConsumer(){
        XxlMqSpringClientFactory xxlMqSpringClientFactory = new XxlMqSpringClientFactory();
        xxlMqSpringClientFactory.setAdminAddress(address);
        xxlMqSpringClientFactory.setAccessToken(token);
        return xxlMqSpringClientFactory;
    }
}
