package com.lzt.crowd.handler;

import com.lzt.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value){
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(key,value);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/set/redis/key/value/with/timeout/remote")
    ResultEntity<String> setRedisKeyValueWithTimeoutRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    ){
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(key,value,time,timeUnit);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/redis/value/by/key/remote")
    ResultEntity<String> getRedisValueByKeyRemote(
            @RequestParam("key") String key
    ){
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String value = operations.get(key);
            return ResultEntity.successWithData(value);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/remove/redis/key/by/key/remote")
    ResultEntity<String> RemoveRedisKeyByKeyRemote(
            @RequestParam("key") String key
    ){
        try {
            stringRedisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
