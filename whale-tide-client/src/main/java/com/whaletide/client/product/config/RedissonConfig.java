package com.whaletide.client.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.context.annotation.Primary;

/**
 * Redisson客户端配置
 * 用于解决历史服务中ZSet操作的问题
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host:localhost}")
    private String host;
    
    @Value("${spring.redis.port:6379}")
    private int port;
    
    @Value("${spring.redis.password:}")
    private String password;
    
    @Value("${spring.redis.database:0}")
    private int database;

    /**
     * 创建Redisson客户端
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        
        // 根据是否有密码配置不同的连接方式
        if (password != null && !password.isEmpty()) {
            config.useSingleServer()
                  .setAddress("redis://" + host + ":" + port)
                  .setPassword(password)
                  .setDatabase(database);
        } else {
            config.useSingleServer()
                  .setAddress("redis://" + host + ":" + port)
                  .setDatabase(database);
        }
        
        // 设置编码
        config.setCodec(new org.redisson.client.codec.StringCodec());
        
        return Redisson.create(config);
    }
    
    /**
     * 创建专用于历史记录服务的RedisTemplate
     */
    @Bean(name = "historyRedisTemplate")
    @Primary
    public RedisTemplate<String, String> historyRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        // 使用简单的String序列化，避免DefaultTuple类问题
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        
        template.afterPropertiesSet();
        return template;
    }
} 