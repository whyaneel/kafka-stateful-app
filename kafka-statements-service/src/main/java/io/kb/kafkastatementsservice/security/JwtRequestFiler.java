package io.kb.kafkastatementsservice.security;

import io.kb.kafkastatementsservice.service.CustomUserDetailsService;
import io.kb.kafkastatementsservice.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

import static io.kb.kafkastatementsservice.utils.Constants.BEARER_TOKEN_PREFIX;

@Component
public class JwtRequestFiler extends OncePerRequestFilter {
  private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFiler.class);
  
  @Autowired private CustomUserDetailsService userDetailsService;
  @Autowired private JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwtToken = null;
    final String authorizationHeader = request.getHeader("Authorization");
    if (null != authorizationHeader && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
      jwtToken = authorizationHeader.substring(BEARER_TOKEN_PREFIX.length());
    }

    if (null != jwtToken
        && jwtService.validateToken(jwtToken)
        && null == SecurityContextHolder.getContext().getAuthentication()) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(
              userDetailsService.loadUserByUsername(jwtService.getSubject(jwtToken)),
              null,
              new HashSet<>());
      usernamePasswordAuthenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    
    filterChain.doFilter(request, response);
  }
}
