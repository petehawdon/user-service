package uk.co.hawdon.demo.users.client;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.hawdon.demo.users.model.User;

@Component
public class RestClient implements Client {

  private static final String USERS_IN_CITY_PATH = "/city/{city}/users";
  private static final String ALL_USERS_PATH = "/users";

  private final String backendServiceScheme;
  private final String backendServiceHost;
  private final RestTemplate restTemplate;

  public RestClient(final RestTemplate restTemplate,
      final @Value("${backend.service.scheme}") String backendServiceScheme,
      final @Value("${backend.service.host}") String backendServiceHost) {
    this.restTemplate = restTemplate;
    this.backendServiceScheme = backendServiceScheme;
    this.backendServiceHost = backendServiceHost;
  }

  @Override
  public Collection<User> getUsersInCity(String city) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme(backendServiceScheme)
        .host(backendServiceHost)
        .path(USERS_IN_CITY_PATH)
        .buildAndExpand(city)
        .encode()
        .toUri();

    return exchange(uri);
  }

  @Override
  public Collection<User> getAllUsers() {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme(backendServiceScheme)
        .host(backendServiceHost)
        .path(ALL_USERS_PATH)
        .build()
        .encode()
        .toUri();

    return exchange(uri);
  }

  private Collection<User> exchange(final URI uri) {
    ResponseEntity<Collection<User>> responseEntity = restTemplate.exchange(uri,
        HttpMethod.GET,
        buildHttpEntity(),
        new ParameterizedTypeReference<Collection<User>>() {
        });
    return responseEntity.getBody();
  }

  private HttpEntity<String> buildHttpEntity() {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.put("Content-Type", Collections.singletonList("application/json"));
    return new HttpEntity<>(headers);
  }

}