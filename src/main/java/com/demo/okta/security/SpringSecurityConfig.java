package com.demo.okta.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserDetailsService customOAuth2UserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf(c -> c
                .disable()
        )
        .authorizeRequests(a -> a
                .antMatchers("/", "/error", "/webjars/**").permitAll()
                .anyRequest().permitAll()
        )
        .logout(l -> l
                .logoutSuccessUrl("/").permitAll()
        )
        .exceptionHandling(e -> e
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        )
        .oauth2Login()
                .successHandler(oktaAuthenticationSuccessHandler())
                .userInfoEndpoint()
                .oidcUserService(customOAuth2UserDetailsService);
    }

    @Bean
    public AuthenticationSuccessHandler oktaAuthenticationSuccessHandler() {
        return new CustomOktaAuthenticationSuccessHandler();
    }
}
