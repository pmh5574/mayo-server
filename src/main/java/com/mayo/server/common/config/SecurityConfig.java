//package com.mayo.server.common.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mayo.server.auth.app.service.JwtTokenProvider;
//import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
//import com.mayo.server.common.security.JwtAuthFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final ChefQueryOutputPort queryOutputPort;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .formLogin().disable()
//                .httpBasic().disable()
//                .csrf(AbstractHttpConfigurer::disable)
//                .headers().frameOptions().disable()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("**/login/**").permitAll()
//                .requestMatchers("**/verify/**").permitAll()
//                .requestMatchers("**/register/**").permitAll()
//                .anyRequest().authenticated();
//
//        http.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//
//    }
//
//    @Bean
//    public JwtAuthFilter jwtAuthenticationProcessingFilter() {
//        return new JwtAuthFilter(jwtTokenProvider, queryOutputPort);
//    }
//
//}
