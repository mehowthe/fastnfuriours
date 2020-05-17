package com.michaldurkalec.fw.fastnfurious.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .httpBasic()
                .and()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/private/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/movies/**", "/v1/movies/**").permitAll()
                    .anyRequest().authenticated();
        //@formatter:on
    }

}
