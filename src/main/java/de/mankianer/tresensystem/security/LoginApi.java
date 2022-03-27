package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.security.models.JwtRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginApi {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  public LoginApi(AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @RequestMapping(value = "/token", method = RequestMethod.POST)
  public  ResponseEntity login(@RequestBody JwtRequest request, HttpServletResponse response) {
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
          JwtTokenUtil.AuthorizationHeaderName + "=Bearer+" + token + "; Max-Age="
              + jwtTokenUtil.JWT_TOKEN_VALIDITY + "; Path=/; HttpOnly; SameSite=None; Secure;");

      return ResponseEntity.accepted().body( token );
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
