package com.ducnt.account.config;

import com.ducnt.account.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    IAccountService accountService;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            accountService.getAdvertisements();
        };
    }
}
