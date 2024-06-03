package com.ph.security.config;

import com.ph.service.UserService;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {

    private final UserService userService;
    private final MessageUtil messageUtil;


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageUtil.getMessage("error.user.update.not-found")));
    }

}
