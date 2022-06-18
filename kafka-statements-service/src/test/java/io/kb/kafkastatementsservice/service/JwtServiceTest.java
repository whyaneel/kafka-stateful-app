package io.kb.kafkastatementsservice.service;

import io.kb.kafkastatementsservice.model.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
  private static final int JWT_VALIDITY_MILLIS = 1000;
  private JwtService jwtService;
  private UserDetails customUserDetails;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService("YOUR_SECRET_KEY", JWT_VALIDITY_MILLIS);
    customUserDetails =
        io.kb.kafkastatementsservice.model.UserDetails.UserDetailsBuilder.anUserDetails()
            .withUserName("foo")
            .withUniqueId("fooId")
            .build();
  }

  @Test
  void whenGivenJwt_AndBeforeValidity_ThenJwtValidationShouldBeSuccess() {
    String jwt = jwtService.generateToken(customUserDetails);
    assertTrue(jwtService.validateToken(jwt));
    assertEquals(customUserDetails.getUniqueId(), jwtService.getId(jwt));
  }

  @Test
  void whenGivenJwt_AndAfterValidity_ThenJwtValidationShouldBeFailed() throws InterruptedException {
    String jwt = jwtService.generateToken(customUserDetails);
    Thread.sleep(JWT_VALIDITY_MILLIS + 1);
    assertFalse(jwtService.validateToken(jwt));
  }
}
