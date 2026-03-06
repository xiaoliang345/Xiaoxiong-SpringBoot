package com.oxn.xiaoxiong.util;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.function.Function;

/**
 * Redis工具类：基于Hutool JSONUtil实现序列化，支持自定义前缀读写对象
 * 标准Spring Bean，非静态，通过@Resource注入StringRedisTemplate
 */
@Component
public class RedisUtil {

    // 注入Spring管理的StringRedisTemplate
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存储对象：自定义前缀+ID，使用Hutool将对象序列化为JSON
     *
     * @param prefix 自定义Redis Key前缀（建议：xxx:）
     * @param id     唯一标识
     * @param obj    待存储的Java对象
     * @param <T>    对象泛型
     */
    public <T> void setConfigObject(String prefix, String id, T obj) {
        try {
            String key = prefix + id;
            // Hutool序列化：对象转JSON字符串
            String jsonStr = JSONUtil.toJsonStr(obj);
            stringRedisTemplate.opsForValue().set(key, jsonStr);
        } catch (Exception e) {
            throw new RuntimeException("对象序列化为JSON失败", e);
        }
    }

    /**
     * 批量存储对象列表：自定义前缀+对象ID，使用Hutool将对象序列化为JSON
     *
     * @param prefix 自定义Redis Key前缀（建议：xxx:）
     * @param list   待存储的对象列表
     * @param idExtractor ID提取函数，用于从对象中获取ID
     * @param <T>    对象泛型
     */
    public <T> void setConfigObjectList(String prefix, List<T> list, Function<T, String> idExtractor) {
        try {
            for (T obj : list) {
                String key = prefix + idExtractor.apply(obj);
                // Hutool序列化：对象转JSON字符串
                String jsonStr = JSONUtil.toJsonStr(obj);
                stringRedisTemplate.opsForValue().set(key, jsonStr);
            }
        } catch (Exception e) {
            throw new RuntimeException("对象序列化为JSON失败", e);
        }
    }

    /**
     * 获取单个对象：自定义前缀+ID，使用Hutool将JSON反序列化为指定类型
     *
     * @param prefix 自定义Redis Key前缀
     * @param id     唯一标识
     * @param clazz  目标对象类型
     * @param <T>    对象泛型
     * @return 反序列化后的对象（不存在返回null）
     */
    public <T> T getConfigObject(String prefix, String id, Class<T> clazz) {
        try {
            String key = prefix + id;
            String jsonStr = stringRedisTemplate.opsForValue().get(key);
            if (jsonStr == null || jsonStr.isEmpty()) {
                return null;
            }
            // Hutool反序列化：JSON字符串转指定类型对象
            return JSONUtil.toBean(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON反序列化为对象失败", e);
        }
    }

    /**
     * 批量获取所有对象：根据自定义前缀匹配Key，返回ID-对象映射
     *
     * @param prefix 自定义Redis Key前缀
     * @param clazz  目标对象类型
     * @param <T>    对象泛型
     * @return Key=业务ID（去掉前缀），Value=对应对象
     */
    public <T> Map<String, T> listAllConfigObjects(String prefix, Class<T> clazz) {
        // SCAN命令匹配前缀Key（非阻塞，生产环境推荐）
        Set<String> keys = stringRedisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keySet = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions()
                    .match(prefix + "*")
                    .count(100)
                    .build());
            while (cursor.hasNext()) {
                keySet.add(new String(cursor.next()));
            }
            return keySet;
        });

        // 批量反序列化
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Map<String, T> resultMap = new HashMap<>();
        for (String key : keys) {
            String id = key.replace(prefix, ""); // 提取纯业务ID
            String jsonStr = ops.get(key);
            if (jsonStr != null && !jsonStr.isEmpty()) {
                try {
                    // Hutool反序列化
                    T obj = JSONUtil.toBean(jsonStr, clazz);
                    resultMap.put(id, obj);
                } catch (Exception e) {
                    throw new RuntimeException("反序列化Key[" + key + "]失败", e);
                }
            }
        }
        return resultMap;
    }

    /**
     * 简化版：批量获取所有对象的列表（仅返回对象，不返回ID）
     *
     * @param prefix 自定义Redis Key前缀
     * @param clazz  目标对象类型
     * @param <T>    对象泛型
     * @return 对象列表
     */
    public <T> List<T> listAllConfigObjectValues(String prefix, Class<T> clazz) {
        Map<String, T> allObjects = listAllConfigObjects(prefix, clazz);
        return new ArrayList<>(allObjects.values());
    }

    /**
     * 删除对象：根据自定义前缀+ID删除Key
     *
     * @param prefix 自定义Redis Key前缀
     * @param id     唯一标识
     */
    public void deleteConfigObject(String prefix, String id) {
        stringRedisTemplate.delete(prefix + id);
    }
}