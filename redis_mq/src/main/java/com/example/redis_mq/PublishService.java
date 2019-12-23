/**
 * @description:
 * @author: lxm
 * @date: 2019/12/16
 **/
package com.example.redis_mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PublishService {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * @param channel 消息发布订阅 主题
     * @param message 消息信息
     */
    public void publish(String channel, Object message) {
        // 该方法封装的 connection.publish(rawChannel, rawMessage);
        redisTemplate.convertAndSend(channel, message);
    }
}