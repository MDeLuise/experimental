package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "index.html", "/css/*", "/js/*")
            .permitAll()
            .antMatchers("/api/**")
            .hasRole(ApplicationUserRole.STUDENT.name())
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails maxUserBuilder = User.builder()
            .username("max")
            .password(passwordEncoder.encode("max"))
            .authorities(ApplicationUserRole.STUDENT.grantedAuthorities())
            .build();

        UserDetails rootUserBuilder = User.builder()
            .username("root")
            .password(passwordEncoder.encode("root"))
            .authorities(ApplicationUserRole.ADMIN.grantedAuthorities())
            .build();

        UserDetails rootTraineeUserBuilder = User.builder()
            .username("rootTrainee")
            .password(passwordEncoder.encode("rootTrainee"))
            .authorities(ApplicationUserRole.ADMINTRAINEE.grantedAuthorities())
            .build();

        return new InMemoryUserDetailsManager(
            maxUserBuilder, rootUserBuilder, rootTraineeUserBuilder);
    }
}
