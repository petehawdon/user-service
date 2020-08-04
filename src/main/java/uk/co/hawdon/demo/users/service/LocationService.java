package uk.co.hawdon.demo.users.service;

public interface LocationService {
  boolean isInProximity(String cityName, double latitude, double longitude);
}
