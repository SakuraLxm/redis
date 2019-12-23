/**
 * @description:
 * @author: lxm
 * @date: 2019/12/16
 **/
package com.example.redis_mq;

import org.springframework.stereotype.Component;

@Component
public class MessageReceiveTwo {
    public void b(String object) {
//序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
//        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(User.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        seria.setObjectMapper(objectMapper);

//        User user = (User) seria.deserialize(object.getBytes());
        System.out.println("消息客户端2号：" + object);
    }
}