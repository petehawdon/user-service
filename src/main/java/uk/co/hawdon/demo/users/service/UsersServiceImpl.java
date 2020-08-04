package uk.co.hawdon.demo.users.service;

import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.hawdon.demo.users.client.RestClient;
import uk.co.hawdon.demo.users.model.User;

@Service
public class UsersServiceImpl implements UsersService {

  private final LocationService locationService;
  private final RestClient restClient;

  @Autowired
  public UsersServiceImpl(final LocationService locationService,
      final RestClient restClient) {
    this.locationService = locationService;
    this.restClient = restClient;
  }

  /**
   * Find Users who are listed as either living in the city requested, or whose current 
   * coordinates are near to the city
   * @param city the city of residence.
   * @return a Collection of Users whom meet the criteria.
   */
  @Override
  public Collection<User> findUsers(final String city) {
    return CollectionUtils.union(
        findUsersInCity(city), findUsersNearCity(city)).stream()
        .sorted()
        .collect(Collectors.toList());
  }

  private Collection<User> findUsersInCity(final String city) {
    return restClient.getUsersInCity(city);
  }

  private Collection<User> findUsersNearCity(final String city) {
    return restClient.getAllUsers().stream()
        .filter(user -> locationService.isInProximity(
            city, user.getLatitude(), user.getLongitude())
        )
        .collect(Collectors.toList());
  }
}
