package uk.co.hawdon.demo.users.service;

import java.util.Collection;
import uk.co.hawdon.demo.users.model.User;

public interface UsersService {

  Collection<User> findUsers(String city);
}
