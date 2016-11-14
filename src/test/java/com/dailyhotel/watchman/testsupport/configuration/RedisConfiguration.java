package com.dailyhotel.watchman.testsupport.configuration;

import com.dailyhotel.watchman.MethodCall;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import javax.inject.Inject;

@Configuration
@EnableCaching(mode = AdviceMode.PROXY, proxyTargetClass = true)
public class RedisConfiguration extends CachingConfigurerSupport {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private RedisConnectionFactory connectionFactory;


    @Override
    @Primary
    @Bean
    public CacheManager cacheManager() {
        return new RedisCacheManager(redisTemplate());
    }

    @Bean
    public RedisTemplate<String, MethodCall> redisTemplate() {
        RedisTemplate<String, MethodCall> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        return template;
    }

}