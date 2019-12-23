/**
 * @description:
 * @author: lxm
 * @date: 2019/12/16
 **/
package com.example.redis_mq;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class SubscribeListener2 implements MessageListener {

    /**
     * 订阅接收发布者的消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 缓存消息是序列化的，需要反序列化。然而new String()可以反序列化，但静态方法valueOf()不可以
        System.out.println(new String(pattern) + "主题发布2：" + new String(message.getBody()));
    }
}