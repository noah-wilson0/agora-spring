package com.agora.debate.debate.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTestServiceTest {

    @Autowired
    private RedisTestService redisTestService;

    @Test
    void redis_set_get() {
        // given
        String key = "testKey";
        String value = "hello, redis!";

        // when
        redisTestService.setValue(key, value);
        String result = redisTestService.getValue(key);

        // then
        assertThat(result).isEqualTo(value);
    }
}
