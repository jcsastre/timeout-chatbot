package com.timeout.chatbot.configuration;

import com.timeout.chatbot.redis.PersonRepo;
import com.timeout.chatbot.redis.PersonRepoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public PersonRepo personRepo() {
        final PersonRepoImpl personRepo = new PersonRepoImpl();
        personRepo.setRedisTemplate(redisTemplate());
        return personRepo;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        final RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnFactory());
        return redisTemplate;
    }

    @Bean
    public JedisConnectionFactory jedisConnFactory() {
        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }


}
