package com.zkrypto.zkMatch.global.redis;

import com.zkrypto.zkMatch.domain.ca.application.dto.response.KeyPair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeyRedisService {
    private final RedisTemplate<String, KeyPair> redisTemplate;

    public void setKeyPair(String key, KeyPair keyPair) {
        redisTemplate.opsForValue().set(key, keyPair);
    }

    public Optional<KeyPair> getKeyPair(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
