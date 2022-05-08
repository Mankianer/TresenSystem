package de.mankianer.tresensystem.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final UserRepository userRepository;

  public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
                        UserRepository userRepository) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain)
      throws ServletException, IOException {
    // Get authorization header and validate
    String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!isBearerToken(requestToken)) {
      if (request.getCookies() != null) {
        requestToken = Arrays.stream(request.getCookies()).filter(c -> c.getName()
                        .equals(JwtTokenUtil.AUTHORIZATION_HEADER_NAME))
                .findFirst().map(c -> c.getValue().replaceFirst("\\+", " ")).orElse(null);
      }
      if (!isBearerToken(requestToken)) {
        chain.doFilter(request, response);
        return;
      }
    }


    // Get jwt token and validate
    final String token = requestToken.split(" ")[1].trim();
    if (Boolean.TRUE.equals(jwtTokenUtil.isTokenExpired(token))) {
      chain.doFilter(request, response);
      return;
    }

    // Get user identity and set it on the spring security context
    UserDetails userDetails = userRepository
        .findByUsername(jwtTokenUtil.getUsernameFromToken(token))
        .orElse(null);

    UsernamePasswordAuthenticationToken
        authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null,
        userDetails == null ?
            List.of() : userDetails.getAuthorities()
    );

    authentication.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private boolean isBearerToken(String requestToken) {
    return requestToken != null && requestToken.startsWith("Bearer ");
  }

}
