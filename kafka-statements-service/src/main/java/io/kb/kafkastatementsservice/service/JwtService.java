package io.kb.kafkastatementsservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.kb.kafkastatementsservice.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {
  private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
  private String jwtSecretKey;
  private long jwtValidityMillis;
  private DefaultJwtSignatureValidator validator;

  public JwtService(
      @Value(value = "${jwt.secret.key}") String jwtSecretKey,
      @Value(value = "${jwt.validity.millis}") long jwtValidityMillis) {
    this.jwtSecretKey = jwtSecretKey;
    this.jwtValidityMillis = jwtValidityMillis;
    this.validator =
        new DefaultJwtSignatureValidator(
            SIGNATURE_ALGORITHM,
            new SecretKeySpec(jwtSecretKey.getBytes(), SIGNATURE_ALGORITHM.getJcaName()));
  }

  public String generateToken(final UserDetails userDetails) {
    long currentTimeMillis = System.currentTimeMillis();
    return Jwts.builder()
        .setClaims(new HashMap<>())
        .setSubject(userDetails.getUserName())
        .setId(userDetails.getUniqueId())
        .setIssuedAt(new Date(currentTimeMillis))
        .setExpiration(new Date(currentTimeMillis + jwtValidityMillis))
        .signWith(
            SIGNATURE_ALGORITHM,
            new SecretKeySpec(jwtSecretKey.getBytes(), SIGNATURE_ALGORITHM.getJcaName()))
        .compact();
  }

  public boolean validateToken(final String jwt) {
    try {
      boolean isExpired = getExpiration(jwt).before(new Date());
      if (isExpired) {
        LOG.info("JWT Expired");
        return false;
      }
    } catch (JwtException e) {
      LOG.error("JWT Expired", e);
      return false;
    }

    try {
      boolean isSignatureValid = validateSignature(jwt);
      if (!isSignatureValid) {
        LOG.info("Failed to verify JWT Integrity");
      }
      return isSignatureValid;
    } catch (JwtException e) {
      LOG.error("Failed to verify JWT Integrity", e);
      return false;
    }
  }

  private boolean validateSignature(final String jwt) {
    String[] chunks = jwt.split("\\.");
    String tokenWithoutSignature = chunks[0] + "." + chunks[1];
    String signature = chunks[2];
    return validator.isValid(tokenWithoutSignature, signature);
  }

  public String getSubject(final String jwt) {
    try {
      return extractClaim(jwt, Claims::getSubject);
    } catch (JwtException e) {
      return null;
    }
  }

  public String getId(final String jwt) {
    return extractClaim(jwt, Claims::getId);
  }

  private Date getExpiration(final String jwt) {
    return extractClaim(jwt, Claims::getExpiration);
  }

  private <T> T extractClaim(final String jwt, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(
        Jwts.parser()
            .setSigningKey(
                new SecretKeySpec(jwtSecretKey.getBytes(), SIGNATURE_ALGORITHM.getJcaName()))
            .parseClaimsJws(jwt)
            .getBody());
  }
}
