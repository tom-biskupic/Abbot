package com.runcible.abbot.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import com.runcible.abbot.service.AuthenticationService;
import java.util.logging.Logger;

@Configuration
@ComponentScan(basePackages = {"com.runcible.abbot"})
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {
    
    final static Logger logger = Logger.getLogger(WebSecurityConfig.class.getName());

    @Autowired
    RestAuthenticationFailureEntryPoint restAuthFailureHandler;

    private AuthenticationService authService;

    public WebSecurityConfig( AuthenticationService authService )
    {
        this.authService = authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception 
    {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
            .userDetailsService(authService)     // configurer
            .passwordEncoder(passwordEncoder());          // still configurer

        return authBuilder.build();  // <-- now called on builder, not configurer
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setHeaderName("X-XSRF-TOKEN");
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        // set the name of the attribute the CsrfToken should be saved to
        requestHandler.setCsrfRequestAttributeName(null); 

        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(tokenRepository)
                .csrfTokenRequestHandler(requestHandler)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/fonts/**",
                    "/views/loginform.html",
                    "/views/welcome.html",
                    "/views/registerform.html",
                    "/views/registersuccessful.html",
                    "/views/contact.html",
                    "/register",
                    "/user",
                    "/user/**",
                    "/webjars/**",
                    "/logout",
                    "/perform_login"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginProcessingUrl("/perform_login")
                .failureHandler(restAuthFailureHandler)
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .permitAll()
            );

        return http.build();
    }
}
