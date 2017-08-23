package com.service;

/**
 * Redis数据库操作接口
 */
public interface RedisService {
    /**
     * 存储key-value数据
     *
     * @param key     键
     * @param value   值
     * @param seconds 过期时间 单位：秒
     * @return 成功返回OK 失败和异常返回null String
     */
    public String putString(String key, String value, int seconds);

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key);

    /**
     * 保存对象
     *
     * @param key     键
     * @param value   值
     * @param seconds 过期时间 单位：秒
     * @return 是否成功
     */
    public String putObj(String key, Object value, int seconds);

    /**
     * 获取对象
     *
     * @param key 键
     * @return 值
     */
    public Object getObj(String key);

    /**
     * 更新token时间
     *
     * @param key          id
     * @param seconds 时间
     * @return
     */
    public Long expire(String key, int seconds);

    /**
     * 删除key的值
     *
     * @param keys key数组
     * @return 删除成功个数
     */
    public Long del(String... keys);
}
