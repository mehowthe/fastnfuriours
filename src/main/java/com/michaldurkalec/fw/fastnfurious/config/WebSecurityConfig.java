package com.michaldurkalec.fw.fastnfurious.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .httpBasic()
                .and()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/h2-console/**").hasRole("ADMIN")
                    .antMatchers("/protected/**").hasRole("MANAGER")
                    .antMatchers("/movies/**").permitAll()
                    .antMatchers("/swagger-ui.html").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .cors()
                .and()
                    .csrf()
                .disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers("/swagger-ui.html");
    }
}
