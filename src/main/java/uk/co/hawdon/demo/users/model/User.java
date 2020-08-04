package uk.co.hawdon.demo.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class User implements Comparable<User>{

  private Integer id;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  private String email;
  @JsonProperty("ip_address")
  private String ipAddress;
  private Double latitude;
  private Double longitude;

  @Override
  public int compareTo(User o) {
    return getId().compareTo(o.getId());
  }
}
