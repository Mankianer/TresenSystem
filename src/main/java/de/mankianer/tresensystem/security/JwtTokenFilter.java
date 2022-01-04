package de.mankianer.tresensystem.security;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final UserRepo userRepo;

  public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
      UserRepo userRepo) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userRepo = userRepo;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain)
      throws ServletException, IOException {
    // Get authorization header and validate
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.isEmpty() || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    // Get jwt token and validate
    final String token = header.split(" ")[1].trim();
    if (!jwtTokenUtil.isTokenExpired(token)) {
      chain.doFilter(request, response);
      return;
    }

    // Get user identity and set it on the spring security context
    UserDetails userDetails = userRepo
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

}
