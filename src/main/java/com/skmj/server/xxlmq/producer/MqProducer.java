package com.skmj.server.xxlmq.producer;

import com.xxl.mq.client.message.XxlMqMessage;
import com.xxl.mq.client.producer.XxlMqProducer;

import java.util.Date;

/**
 * 生产消息
 *
 * @author lc
 */
public class MqProducer {
    /**
     * 发送消息
     * @param topic topic
     * @param msg 消息
     */
    public void send(String topic, String msg) {
        XxlMqMessage mqMessage = new XxlMqMessage();
        mqMessage.setTopic(topic);
        mqMessage.setData(msg);
        mqMessage.setShardingId(1);
        XxlMqProducer.produce(mqMessage);

    }
    /**
     * 发送延时消息
     * @param topic topic
     * @param msg 消息
     * @param date 生效时间
     */
    public void send(String topic, String msg, Date date) {
        XxlMqMessage mqMessage = new XxlMqMessage();
        mqMessage.setTopic(topic);
        mqMessage.setData(msg);
        mqMessage.setEffectTime(date);
        XxlMqProducer.produce(mqMessage);

    }
}
