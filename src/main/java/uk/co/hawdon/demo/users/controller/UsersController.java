package uk.co.hawdon.demo.users.controller;

import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.co.hawdon.demo.users.model.User;
import uk.co.hawdon.demo.users.service.UsersService;

@RestController
public class UsersController implements UsersApi {

  private UsersService usersService;
  
  public UsersController(UsersService usersService) {
    this.usersService = usersService;
  }

  @Override
  @GetMapping(path = "/v1/users/{city}", produces = "application/json")
  public ResponseEntity<Collection<User>> getUsersByCity(@PathVariable String city) {
    return new ResponseEntity<>(usersService.findUsers(city), HttpStatus.OK);
  }
}
