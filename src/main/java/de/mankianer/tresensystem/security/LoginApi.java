package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.security.models.JwtRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginApi {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  public LoginApi(AuthenticationManager authenticationManager,
                  JwtTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/token")
  public ResponseEntity<String> login(@RequestBody JwtRequest request, HttpServletResponse response) {
    try {
      Authentication authenticate = authenticationManager
              .authenticate(
                      new UsernamePasswordAuthenticationToken(
                              request.getUsername(), request.getPassword()
                      )
              );

      User user = (User) authenticate.getPrincipal();
      String token = jwtTokenUtil.generateToken(user);

      response.setHeader("Set-Cookie",
              JwtTokenUtil.AUTHORIZATION_HEADER_NAME + "=Bearer+" + token + "; Max-Age="
                      + jwtTokenUtil.jwtTokenValidity + "; Path=/; HttpOnly; SameSite=None; Secure;");

      return ResponseEntity.ok().body( token );
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
