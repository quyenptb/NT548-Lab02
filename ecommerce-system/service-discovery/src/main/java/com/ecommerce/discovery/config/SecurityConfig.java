package com.ecommerce.discovery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. QUAN TRỌNG: Phải tắt CSRF thì Eureka Client mới gửi request POST tới được
            .csrf(csrf -> csrf.disable())
            
            // 2. Cho phép mọi yêu cầu đã xác thực (với user/pass admin)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            
            // 3. Sử dụng HTTP Basic Authentication (để truyền user:pass qua URL)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
