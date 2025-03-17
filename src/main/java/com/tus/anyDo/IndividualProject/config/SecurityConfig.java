package com.tus.anyDo.IndividualProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tus.anyDo.IndividualProject.filter.JWTAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	private JWTAuthenticationFilter jwtFilter;
	
	public SecurityConfig(JWTAuthenticationFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

    // Define a PasswordEncoder bean for your application.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure the security filter chain.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> 
            	auth
            	.requestMatchers("/api/auth/**").permitAll()
            	.requestMatchers("/").permitAll()
            	.requestMatchers("/assets/**").permitAll()
            	.requestMatchers("/components/**").permitAll()
            	.requestMatchers("/css/**").permitAll()
            	.requestMatchers("/js/**").permitAll()
            	.requestMatchers("/index.html").permitAll()
            	.requestMatchers("/script.js").permitAll()
            	.requestMatchers("/styles.css").permitAll()
            	.anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
            .formLogin(form -> form.disable()) // Disable form login
            .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic auth
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

}