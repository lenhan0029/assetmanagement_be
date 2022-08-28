package com.nashtech.rookies.AssetManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nashtech.rookies.AssetManagement.security.jwt.JwtAuthEntryPoint;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtAuthTokenFilter;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtUtils;
import com.nashtech.rookies.AssetManagement.security.service.UserDetailsServiceImpl;






@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)


public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    final private JwtAuthEntryPoint unauthorizedHandler;

    private final JwtUtils jwtUtils;

    public WebSecurityConfig (UserDetailsServiceImpl userDetailsService, JwtAuthEntryPoint unauthorizedHandler, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }
    
    

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // TODO
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers("/api/login", "/api/public/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/account/**").hasAuthority("admin")
            .antMatchers(HttpMethod.GET, "/api/account/**").hasAuthority("admin")
            .antMatchers(HttpMethod.POST, "/api/asset").hasAuthority("admin")
            .antMatchers(HttpMethod.POST,"/api/category").hasAuthority("admin")
            .antMatchers(HttpMethod.POST, "/api/category/**").hasAuthority("admin")
            .antMatchers(HttpMethod.POST,"/api/asset").hasAuthority("admin")
            .antMatchers(HttpMethod.GET,"/api/assignment").hasAuthority("admin")
            .antMatchers(HttpMethod.POST,"/api/assignment").hasAuthority("admin")
            .antMatchers(HttpMethod.GET,"/api/category/**").hasAuthority("admin")
            .antMatchers(HttpMethod.GET,"/api/asset/**").hasAuthority("admin")
            .antMatchers(HttpMethod.GET,"/api/information/**").permitAll()
            .antMatchers(HttpMethod.PUT,"/api/asset/**").hasAuthority("admin")
            .antMatchers(HttpMethod.PUT,"/api/information/**").hasAuthority("admin")
                .antMatchers(HttpMethod.GET,"/api/request/**").hasAuthority("admin")
                .antMatchers(HttpMethod.PUT,"/api/request/**").hasAuthority("admin")
                .antMatchers(HttpMethod.DELETE,"/api/request/**").hasAuthority("admin")
                .antMatchers(HttpMethod.GET, "/api/assignment/user").hasAnyAuthority("staff", "admin")
                .antMatchers(HttpMethod.POST, "/api/request/**").hasAnyAuthority("staff", "admin")
        	.anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
