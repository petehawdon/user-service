package uk.co.hawdon.demo.users.client;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.co.hawdon.demo.users.TestHelper;
import uk.co.hawdon.demo.users.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class RestClientTest {

  private TestHelper testHelper = new TestHelper();
  
  @Value("${backend.service.scheme}")
  private String backendServiceScheme;
  @Value("${backend.service.host}")
  private String backendServiceHost;
  
  private RestClient restClient;
  private RestTemplate mockedRestTemplate;
  private URI urlByCity = null;
  private URI urlUsers = null;

  @Before
  public void setUp(){
    mockedRestTemplate = mock(RestTemplate.class);
    restClient = new RestClient(mockedRestTemplate, backendServiceScheme, backendServiceHost);
    urlByCity = URI.create(backendServiceScheme + "://" + backendServiceHost+ "/city/London/users");
    urlUsers = URI.create(backendServiceScheme + "://" + backendServiceHost+ "/users");
  }

  @Test
  public void givenSomeUsersExistWithCityDeclaredAsLondonWhenClientIsCalledThenReturnsThoseUsers() throws Exception {

    ResponseEntity<Object> mockedResponseEntityInLondon = mock(ResponseEntity.class);
    when(mockedRestTemplate.exchange(
        eq(urlByCity),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        any(ParameterizedTypeReference.class)))
        .thenReturn(mockedResponseEntityInLondon);

    Collection<User> users = testHelper.getUsers("/usersInLondon.json");
    when(mockedResponseEntityInLondon.getBody()).thenReturn(users);
    
    Collection<User> actual = restClient.getUsersInCity("London");

    verify(mockedRestTemplate, times(1)).exchange(eq(urlByCity),
        eq(HttpMethod.GET), any(HttpEntity.class),
        any(ParameterizedTypeReference.class));
    
    assertTrue("Should contain the six Users in London",
        CollectionUtils.isEqualCollection(actual, users));
  }

  @Test
  public void givenSomeUsersExistInProximityToLondonWhenClientIsCalledThenReturnsThoseUsers() throws Exception {

    ResponseEntity<Object> mockedResponseEntityNearLondon = mock(ResponseEntity.class);
    when(mockedRestTemplate.exchange(
        eq(urlUsers),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        any(ParameterizedTypeReference.class)))
        .thenReturn(mockedResponseEntityNearLondon);

    Collection<User> usersInProximity = testHelper.getUsers("/usersInProximityToLondon.json");
    when(mockedResponseEntityNearLondon.getBody()).thenReturn(usersInProximity);

    Collection<User> actual = restClient.getAllUsers();

    verify(mockedRestTemplate, times(1)).exchange(eq(urlUsers),
        eq(HttpMethod.GET), any(HttpEntity.class),
        any(ParameterizedTypeReference.class));

    assertTrue("Should contain the three Users in proximity",
        CollectionUtils.isEqualCollection(actual, usersInProximity));
  }
}