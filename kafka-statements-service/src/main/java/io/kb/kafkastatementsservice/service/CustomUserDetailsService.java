package io.kb.kafkastatementsservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private static final Logger LOG = LoggerFactory.getLogger(CustomUserDetailsService.class);

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOG.info("Username: {}", username);
    // Accept any user with password "bar", for simplicity
    return new User(username, "bar", new ArrayList<>());
  }

  public io.kb.kafkastatementsservice.model.UserDetails getUserDetails(String username) {
    // Unique Id varies dynamically per user, may be encrypted, in Ideal Case
    String uniqueId = "P-0000000000";
    if (username.trim().equalsIgnoreCase("Anil")) {
      uniqueId = "P-0123456789";
    } else if (username.trim().equalsIgnoreCase("Kumar")) {
      uniqueId = "P-9876543210";
    }
    LOG.info("UniqueId [{}] derived for User [{}]", uniqueId, username);
    return io.kb.kafkastatementsservice.model.UserDetails.UserDetailsBuilder.anUserDetails()
        .withUserName(username)
        .withUniqueId(uniqueId)
        .build();
  }
}
