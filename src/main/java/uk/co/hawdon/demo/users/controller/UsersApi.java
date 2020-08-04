package uk.co.hawdon.demo.users.controller;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import uk.co.hawdon.demo.users.model.User;

/**
 * Defines the Users api as an interface.
 */
public interface UsersApi {

  /**
   * 
   * @param city the city of residence.
   * @return a collection of Users who declare they are resident in the requested city or who are within
   *   a defined distance of that city.
   */
  ResponseEntity<Collection<User>> getUsersByCity(String city);
}
