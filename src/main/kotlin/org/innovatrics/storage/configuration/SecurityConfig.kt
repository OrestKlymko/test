package org.innovatrics.storage.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): DefaultSecurityFilterChain? {
        return httpSecurity
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .cors { it.disable() }
            .csrf { it.disable() }
            .build()
    }
}
