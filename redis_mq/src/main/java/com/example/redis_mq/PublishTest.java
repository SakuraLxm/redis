/**
 * @description: 消息发布
 * @author: lxm
 * @date: 2019/12/16
 **/
package com.example.redis_mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RedisMqApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PublishTest {

    @Autowired
    private PublishService publishService;

    @Test
    public void test() {
        for(int i=1; i<=10; i++){
            //向dj主题里发布10个消息
            publishService.publish("dj", "like "+i+" 次");
//            publishService.publish("dj2", "like "+i+" 次");
        }
    }
}