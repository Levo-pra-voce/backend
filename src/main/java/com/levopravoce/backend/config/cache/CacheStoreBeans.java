package com.levopravoce.backend.config.cache;

import com.levopravoce.backend.common.cache.CacheStore;
import com.levopravoce.backend.services.user.dto.PasswordRestore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStoreBeans {

    @Bean
    public CacheStore<PasswordRestore> passwordRestoreCache() {
        return new CacheStore<>(10, TimeUnit.MINUTES);
    }

}