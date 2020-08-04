package uk.co.hawdon.demo.users.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.hawdon.demo.users.TestHelper;
import uk.co.hawdon.demo.users.client.RestClient;
import uk.co.hawdon.demo.users.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class UsersServiceTest {

  private TestHelper testHelper = new TestHelper();

  private UsersService usersService;
  private LocationService mockedLocationService;
  private RestClient mockedRestClient;

  @Before
  public void setUp(){
    mockedLocationService = mock(LocationServiceImpl.class);
    mockedRestClient = mock(RestClient.class);
    usersService = new UsersServiceImpl(mockedLocationService, mockedRestClient);
  }

  @Test
  public void givenSomeUsersExistWithCityDeclaredAsLondonButNoneInProximityWhenServiceIsCalledThenReturnsThoseUsers() throws Exception {

    Collection<User> users = testHelper.getUsers("/usersInLondon.json");
    when(mockedRestClient.getUsersInCity(eq("London")))
        .thenReturn(users);

    Collection<User> emptyUsers = testHelper.getUsers("/emptyArray.json");
    when(mockedRestClient.getAllUsers())
        .thenReturn(emptyUsers);

    Collection<User> actual = usersService.findUsers("London");

    verify(mockedRestClient, times(1)).getUsersInCity(eq("London"));
    verify(mockedRestClient, times(1)).getAllUsers();
    
    assertTrue("should contain the six Users in London",
        CollectionUtils.isEqualCollection(actual, users));
  }

  @Test
  public void givenSomeUsersExistInProximityToLondonButNoneWithCityDeclaredAsLondonWhenServiceIsCalledThenReturnsThoseUsers() throws Exception {

    Collection<User> emptyUsers = testHelper.getUsers("/emptyArray.json");
    when(mockedRestClient.getUsersInCity(eq("London"))).thenReturn(emptyUsers);

    Collection<User> users = testHelper.getUsers("/usersInProximityToLondon.json");
    when(mockedRestClient.getAllUsers()).thenReturn(users);
    when(mockedLocationService.isInProximity(eq("London"), anyDouble(), anyDouble())).thenReturn(true);

    Collection<User> actual = usersService.findUsers("London");

    verify(mockedRestClient, times(1)).getUsersInCity(eq("London"));
    verify(mockedRestClient, times(1)).getAllUsers();

    assertTrue("should contain the three Users near London",
        CollectionUtils.isEqualCollection(actual, users));
  }

  @Test
  public void givenSomeUsersExistButAreNotInProximityToLondonAndNoneWithCityDeclaredAsLondonWhenServiceIsCalledThenReturnsEmptyCollection() throws Exception {

    Collection<User> emptyUsers = testHelper.getUsers("/emptyArray.json");
    when(mockedRestClient.getUsersInCity(eq("London"))).thenReturn(emptyUsers);

    Collection<User> users = testHelper.getUsers("/usersNotInProximityToLondon.json");
    when(mockedRestClient.getAllUsers()).thenReturn(users);
    when(mockedLocationService.isInProximity(eq("London"), anyDouble(), anyDouble())).thenReturn(false);

    Collection<User> actual = usersService.findUsers("London");

    verify(mockedRestClient, times(1)).getUsersInCity(eq("London"));
    verify(mockedRestClient, times(1)).getAllUsers();

    assertTrue("should contain no Users",
        CollectionUtils.isEmpty(actual));
  }
}