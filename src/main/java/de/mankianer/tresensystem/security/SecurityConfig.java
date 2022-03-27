package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
        .antMatchers( "/order**").permitAll()
        .antMatchers(HttpMethod.GET, "/product*/**").permitAll()
        .antMatchers(HttpMethod.POST, "/user*/**").permitAll()
        .antMatchers(HttpMethod.POST, "/bar/**").permitAll()
        // Our private endpoints
        .antMatchers(HttpMethod.GET, "/product*/**").hasAuthority(AuthorityEnum.USER.name())
        .antMatchers(HttpMethod.POST, "/user*/**").hasAuthority(AuthorityEnum.USER.name())
        .antMatchers(HttpMethod.POST, "/bar/**").hasAuthority(AuthorityEnum.BARKEEPER.name())
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
