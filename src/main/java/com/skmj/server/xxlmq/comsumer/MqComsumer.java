package com.skmj.server.xxlmq.comsumer;

import com.xxl.mq.client.consumer.IMqConsumer;
import com.xxl.mq.client.consumer.MqResult;
import com.xxl.mq.client.consumer.annotation.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消费消息
 *
 * @author lc
 */
@Slf4j
@MqConsumer(topic = "topic_1")
@Service
public class MqComsumer implements IMqConsumer {
    @Override
    public MqResult consume(String data) throws Exception {
        log.info("[MqComsumer] 消费一条消息:{}", data);
        return MqResult.SUCCESS;
    }
}
