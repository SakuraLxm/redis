package com.example.redis_mq;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置

 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport{

	private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    	log.info("开始");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        GenericFastJsonRedisSerializer fastSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setValueSerializer(fastSerializer);
        redisTemplate.setHashValueSerializer(fastSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
    /**
     * @author 描述：需要手动定义RedisMessageListenerContainer加入IOC容器
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory,
                                                                       MessageListenerAdapter listenerAdapter,MessageListenerAdapter messageListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);

        /**
         * 添加订阅者监听类，数量不限.PatternTopic定义监听主题,这里监听dj主题
         */
        container.addMessageListener(new SubscribeListener(), new PatternTopic("dj"));
        container.addMessageListener(new SubscribeListener(), new PatternTopic("dj2"));

        /**
         * 通过自定义适配器去监听消费
         */
        container.addMessageListener(listenerAdapter,new PatternTopic("dj"));
        container.addMessageListener(messageListenerAdapter,new PatternTopic("dj2"));
        return container;

    }
    @Bean
    MessageListenerAdapter messageListenerAdapter(MessageReceiveTwo receiver){
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“MessageReceiveTwo ”


        return new MessageListenerAdapter(receiver,"b");
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiveOne receiver){
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“MessageReceiveTwo ”
        return new MessageListenerAdapter(receiver,"a");
    }
//    @Bean
//    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForHash();
//    }

    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

//    @Bean
//    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForSet();
//    }
//
//    @Bean
//    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForZSet();
//    }
    /*要启用spring缓存支持,需创建一个 CacheManager的 bean，CacheManager 接口有很多实现，这里Redis 的集成，用 RedisCacheManager这个实现类
  Redis 不是应用的共享内存，它只是一个内存服务器，就像 MySql 似的，
  我们需要将应用连接到它并使用某种“语言”进行交互，因此我们还需要一个连接工厂以及一个 Spring 和 Redis 对话要用的 RedisTemplate，
  这些都是 Redis 缓存所必需的配置，把它们都放在自定义的 CachingConfigurerSupport 中
   */
//    @Bean
//    public CacheManager cacheManager(
//            @SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        cacheManager.setDefaultExpiration(60);//设置缓存保留时间（seconds）
//        return cacheManager;
//    }

//    @Bean
//    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
//        String[] cacheNames = {"config", "users", "blogs", "sysUser", "userList", "info"};
//        redisTemplate.setKeySerializer(new GenericToStringSerializer<Object>(Object.class));
//        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
////        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate, Arrays.asList(cacheNames));
//        redisCacheManager.setDefaultExpiration(86400);
//        redisCacheManager.setUsePrefix(true);
//        redisCacheManager.setCachePrefix(new RedisCachePrefix() {
//            private final RedisSerializer<String> serializer = new StringRedisSerializer();
//            private final String delimiter = ":";
//
//            public byte[] prefix(String cacheName) {
//                return this.serializer
//                        .serialize(cacheName.concat(this.delimiter));
//            }
//        });
//        return redisCacheManager;
//    }
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24)); // 设置缓存有效期一小时
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

}
