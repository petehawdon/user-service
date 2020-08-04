package uk.co.hawdon.demo.users.client;

import java.util.Collection;
import uk.co.hawdon.demo.users.model.User;

public interface Client {
  Collection<User> getAllUsers();
  Collection<User> getUsersInCity(String city);
}
