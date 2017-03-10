package com.timeout.chatbot.configuration;

import com.timeout.chatbot.session.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, Session> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }

//    @Bean
//    public RedisTemplate<?, ?> redisTemplate() {
//        RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
//        template.setConnectionFactory(connectionFactory());
//        return template;
//    }

//    @Bean
//    public JedisConnectionFactory jedisConnFactory() {
//        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setUsePool(true);
//        return jedisConnectionFactory;
//    }

//    @Bean
//    public PersonRepo personRepo() {
//        final PersonRepoImpl personRepo = new PersonRepoImpl();
//        personRepo.setRedisTemplate(redisTemplate());
//        return personRepo;
//    }
//
//    @Bean
//    public RedisTemplate<String, com.timeout.chatbot.redis.Person> redisTemplate() {
//        final RedisTemplate<String, com.timeout.chatbot.redis.Person> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnFactory());
//        return redisTemplate;
//    }
//
//    @Bean
//    public JedisConnectionFactory jedisConnFactory() {
//        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setUsePool(true);
//        return jedisConnectionFactory;
//    }
}
