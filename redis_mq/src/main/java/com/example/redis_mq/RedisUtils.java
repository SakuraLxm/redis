package com.example.redis_mq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-17 21:12
 */
@Component
public class RedisUtils {
    /**
     * 0.config
     */


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 这里可以重新设置规则然后不用多次进行 fastJson
     */
    @Autowired
    private ValueOperations<String, String> valueOperations;

    // redis的消息队列直接使用redis数组实现
    @Autowired
    private ListOperations<String, Object> listOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE=60 * 60 * 2;
    public final static long SMALL_DEFAULT_EXPIRE=60 * 60 * 1;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE=-1;

    public void set(String key,Object value,long expire) {
        valueOperations.set(key,toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key,expire,TimeUnit.SECONDS);
        }
        if (value == null) {
            redisTemplate.expire(key,60,TimeUnit.SECONDS);

        }

    }


    public void setLeft(String key,Object value,long expire) {
        valueOperations.set(key,toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key,expire,TimeUnit.SECONDS);
        }
        if (value == null) {
            redisTemplate.expire(key,60,TimeUnit.SECONDS);

        }

    }

    public void set(String key,Object value) {
        set(key,value,DEFAULT_EXPIRE);
    }

    public <T> T get(String key,Class<T> clazz,long expire) {
        String value=valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key,expire,TimeUnit.SECONDS);

        }
        return value == null ? null : JSONObject.parseObject(value,clazz);
    }

    public <T> T get(String key,Class<T> clazz) {
        return get(key,clazz,NOT_EXPIRE);
    }

    public <T> List<T> getArray(String key,Class<T> t) {
        String value=valueOperations.get(key);
        List<T> result=null;
        if (value == null || value.equals("[]")) {
            result=ListUtil.empty();
        } else {
            try {
                result=JSONObject.parseArray(value,t);
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
        return result;
    }

    public String get(String key,long expire) {
        String value=valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
//            redisTemplate.
            redisTemplate.expire(key,expire,TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key,NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deleteAll(List<String> list) {
        redisTemplate.delete(list);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */

    /**
     * 根据前缀删除key
     *
     * @param prex
     */
    public void deleteByPrex(String prex) {
        //org.apache.commons.collections.CollectionUtils

        prex=prex + "**";
        Set<String> keys=redisTemplate.keys(prex);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }


    public Boolean hasKey(String key) {
        //org.apache.commons.collections.CollectionUtils

        return redisTemplate.hasKey(key);
    }
}
