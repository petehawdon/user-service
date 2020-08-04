package uk.co.hawdon.demo.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import uk.co.hawdon.demo.users.model.User;

public class TestHelper {

  public Collection<User> getUsers(final String jsonFile) throws IOException {
    try(InputStream is = this.getClass().getResourceAsStream(jsonFile)) {
      return new ObjectMapper().readValue(is, new TypeReference<Collection<User>>() { });
    }
  }
}
