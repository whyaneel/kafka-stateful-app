package io.kb.kafkastatementsservice.model;

public class UserDetails {
  private String userName;
  private String uniqueId;

  public String getUserName() {
    return userName;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public static final class UserDetailsBuilder {
    private String userName;
    private String uniqueId;

    private UserDetailsBuilder() {}

    public static UserDetailsBuilder anUserDetails() {
      return new UserDetailsBuilder();
    }

    public UserDetailsBuilder withUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public UserDetailsBuilder withUniqueId(String uniqueId) {
      this.uniqueId = uniqueId;
      return this;
    }

    public UserDetails build() {
      UserDetails userDetails = new UserDetails();
      userDetails.uniqueId = this.uniqueId;
      userDetails.userName = this.userName;
      return userDetails;
    }
  }
}
