package uk.co.hawdon.demo.users.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class LocationServiceTest {

  @Value("${proximity.distance.miles}")
  private Double proximityDistance;
  
  @Test
  public void returnsFalseWhenNotInProximityToLondon() {
    LocationService service = new LocationServiceImpl(proximityDistance);
    assertFalse(service.isInProximity("London", -100.0, -100.0));
  }

  @Test
  public void returnsTrueWhenInProximityToLondon() {
    LocationService service = new LocationServiceImpl(proximityDistance);
    assertTrue(service.isInProximity("London", 51.0, 0.0));
  }

  @Test
  public void returnsFalseWhenUnknownCity() {
    LocationService service = new LocationServiceImpl(proximityDistance);
    assertFalse(service.isInProximity("unknown", 51.0, 0.0));
  }
}