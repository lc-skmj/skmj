package com.skmj.server.xxlmq.demo;

import com.skmj.server.xxlmq.producer.MqProducer;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * @author lc
 */
public class DemoController {

    /**
     * 发送消息
     */
    @GetMapping("send")
    public void send() {
        MqProducer.send("topic_1", "hello world");
    }
    /**
     * 发送定时消息
     */
    @GetMapping("sendDelay")
    public void sendDelay() {
        MqProducer.send("topic_1", "hello world", new Date(System.currentTimeMillis() + 1000 * 10));
    }
}
