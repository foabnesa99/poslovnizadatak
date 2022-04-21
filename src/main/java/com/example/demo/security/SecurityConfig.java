package com.example.demo.security;

import com.example.demo.jwt.AuthorizationFilter;
import com.example.demo.jwt.CustomAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static java.lang.invoke.VarHandle.AccessMode.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManagerBean());
        http.csrf().disable();
        http.formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/homepage", true)
                .failureUrl("/login.html?error=true").and().logout().logoutUrl("/perform_logout").deleteCookies("JSESSIONID");
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/", "/login", "/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/playlists/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthFilter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
