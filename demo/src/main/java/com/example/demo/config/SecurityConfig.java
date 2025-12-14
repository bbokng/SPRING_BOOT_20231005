package com.example.demo.config;

import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final MemberUserDetailsService memberUserDetailsService;
    private final Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final boolean isDev = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> "dev".equalsIgnoreCase(profile));

        http
                .headers(headers -> headers.addHeaderWriter((request, response) ->
                        response.setHeader("X-XSS-Protection", "1; mode=block")))
                .csrf(csrf -> {
                    if (isDev) {
                        csrf.disable();
                    }
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/board_list",
                                "/join_new",
                                "/member_login",
                                "/api/members",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/img/**",
                                "/lib/**",
                                "/scss/**",
                                "/favicon.ico"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/member_login")
                        .loginProcessingUrl("/api/login_check")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            log.info("로그인 성공 - email: {}", authentication.getName());
                            String sessionId = UUID.randomUUID().toString();
                            String email = authentication.getName();
                            request.getSession().setAttribute("userId", sessionId);
                            request.getSession().setAttribute("email", email);
                            request.getSession().setAttribute("userId:" + email, sessionId);
                            request.getSession().setAttribute("email:" + email, email);
                            response.sendRedirect("/board_list");
                        })
                        .failureUrl("/member_login?error=true")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                        .key("demo-remember-me-key")
                        .tokenValiditySeconds(60 * 60 * 24 * 14)
                        .userDetailsService(memberUserDetailsService)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                log.info("로그아웃 성공 - email: {}", authentication.getName());
                            } else {
                                log.info("로그아웃 성공 - anonymous");
                            }
                            response.sendRedirect("/board_list");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/session-expired")
                        .maximumSessions(-1) // 여러 사용자가 동시에 로그인 가능
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
