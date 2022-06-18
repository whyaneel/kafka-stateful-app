package io.kb.kafkastatementsservice.controller;

import io.kb.kafkastatementsservice.exception.BadCredentialsException;
import io.kb.kafkastatementsservice.model.AuthenticationRequest;
import io.kb.kafkastatementsservice.service.CustomUserDetailsService;
import io.kb.kafkastatementsservice.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static io.kb.kafkastatementsservice.utils.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = API_CONTEXT + VERSION, produces = APPLICATION_JSON_VALUE)
public class AuthenticationController {
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

  private AuthenticationManager authenticationManager;
  private CustomUserDetailsService userDetailsService;
  private JwtService jwtService;

  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      CustomUserDetailsService userDetailsService,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
  }

  @PostMapping(
      path = AUTHENTICATE_API,
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.getUsername(), authenticationRequest.getPassword()));
      LOG.info("Authentication Success for user [{}]", authenticationRequest.getUsername());
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException(
          "Authentication Failed for user [" + authenticationRequest.getUsername() + "]");
    }
    return jwtService.generateToken(
        userDetailsService.getUserDetails(authenticationRequest.getUsername()));
  }
}
