package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Log4j2
@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserRepository userRepository;
  private final JwtTokenFilter jwtTokenFilter;

  public SecurityConfig(UserRepository userRepository, JwtTokenFilter jwtTokenFilter) {
    this.userRepository = userRepository;
    this.jwtTokenFilter = jwtTokenFilter;
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(username -> userRepository
        .findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User: " + username + " not found")
        ));
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Enable CORS and disable CSRF
    http = http.cors().and().csrf().disable();

    // Set session management to stateless
    http = http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and();
    http.headers().frameOptions().sameOrigin();

    // Set unauthorized requests exception handler
    http = http
        .exceptionHandling()
        .authenticationEntryPoint(
            (request, response, ex) -> {
              response.sendError(
                  HttpServletResponse.SC_UNAUTHORIZED,
                  ex.getMessage()
              );
            }
        )
        .and();

    // Set permissions on endpoints
    http.authorizeRequests()
        // Our public endpoints
        .antMatchers("/h2-console*/**").permitAll()
        .antMatchers(HttpMethod.POST,"/token").permitAll()
        .antMatchers( "/v3/**").permitAll()
        .antMatchers( "/product*/**").permitAll()
        .antMatchers( "/user*/**").permitAll()
        .antMatchers( "/bar/**").permitAll()
        .antMatchers("/order*/**").permitAll()
        // Our private endpoints
        .antMatchers( "/order*/**").hasAuthority(AuthorityEnum.USER.name())
        .antMatchers(HttpMethod.POST, "/bar/**").hasAuthority(AuthorityEnum.BARKEEPER.name())
        .antMatchers("/user/**").hasAuthority(AuthorityEnum.USER.name())
        //product endpoints
        .antMatchers(HttpMethod.POST, "/product*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        .antMatchers(HttpMethod.PUT, "/product*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        .antMatchers(HttpMethod.DELETE, "/product*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        //users endpoints
        .antMatchers(HttpMethod.GET, "/admin*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        .antMatchers(HttpMethod.POST, "/admin*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        .antMatchers(HttpMethod.PUT, "/admin*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        .antMatchers(HttpMethod.DELETE, "/admin*/**").hasAuthority(AuthorityEnum.ADMIN.name())
        .anyRequest().authenticated();

    // Add JWT token filter
    http.addFilterBefore(
        jwtTokenFilter,
        UsernamePasswordAuthenticationFilter.class
    );
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Used by spring security if CORS is enabled.
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
