package uk.co.hawdon.demo.users.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.hawdon.demo.users.TestHelper;
import uk.co.hawdon.demo.users.model.User;
import uk.co.hawdon.demo.users.service.UsersService;
import uk.co.hawdon.demo.users.service.UsersServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {

  private UsersController usersController;
  private UsersService mockedUsersService;
  private TestHelper testHelper = new TestHelper();

  @Before
  public void setup() {
    mockedUsersService = mock(UsersServiceImpl.class);
    usersController = new UsersController(mockedUsersService);
  }

  @Test
  public void givenSomeLondonUsersExistWhenControllerIsCalledThenReturnsThoseUsersAndStatusCode200()
      throws IOException {
    Collection<User> users = testHelper.getUsers("/usersInLondon.json");
    ResponseEntity<Collection<User>> expected = new ResponseEntity<>(users, HttpStatus.OK);
    when(mockedUsersService.findUsers("London")).thenReturn(users);
    ResponseEntity<Collection<User>> actual = usersController.getUsersByCity("London");
    assertEquals(expected, actual);
  }

  @Test
  public void givenNoLondonUsersExistWhenControllerIsCalledThenReturnsEmptyCollectionAndStatusCode200()
      throws IOException {
    Collection<User> users = testHelper.getUsers("/emptyArray.json");
    ResponseEntity<Collection<User>> expected = new ResponseEntity<>(users, HttpStatus.OK);
    when(mockedUsersService.findUsers("London")).thenReturn(users);
    ResponseEntity<Collection<User>> actual = usersController.getUsersByCity("London");
    assertEquals(expected, actual);
  }
}