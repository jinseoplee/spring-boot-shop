package com.ljs.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    private static final String SIGNIN_URL = "/users/signin";
    private static final String SIGNIN_ERROR_URL = "/users/signin/error";
    private static final String LOGOUT_URL = "/users/logout";
    private static final String CSS_URL = "/css/**";
    private static final String JS_URL = "/js/**";
    private static final String IMG_URL = "/img/**";
    private static final String ROOT_URL = "/";
    private static final String USERS_URL = "/users/**";
    private static final String ADMIN_URL = "/admin/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 로그인 설정
                .formLogin((form) -> form
                        .loginPage(SIGNIN_URL)
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl(SIGNIN_ERROR_URL))

                // 로그아웃 설정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                        .logoutSuccessUrl("/"))

                // URL 권한 설정
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(CSS_URL, JS_URL, IMG_URL, ROOT_URL, USERS_URL).permitAll()
                        .requestMatchers(ADMIN_URL).hasRole("ADMIN")
                        .anyRequest().authenticated())

                // 예외 처리 설정
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }
}