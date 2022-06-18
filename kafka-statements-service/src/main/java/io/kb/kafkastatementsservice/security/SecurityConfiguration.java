package io.kb.kafkastatementsservice.security;

import io.kb.kafkastatementsservice.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.kb.kafkastatementsservice.utils.Constants.*;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);
  @Autowired private CustomUserDetailsService customUserDetailsService;
  @Autowired private JwtRequestFiler jwtRequestFiler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeHttpRequests(
            authz ->
                authz
                    .antMatchers(API_CONTEXT + VERSION + AUTHENTICATE_API)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .userDetailsService(customUserDetailsService)
        .httpBasic(withDefaults())
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(jwtRequestFiler, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    LOG.info("PasswordEncoder called");
    return NoOpPasswordEncoder.getInstance();
  }
}
