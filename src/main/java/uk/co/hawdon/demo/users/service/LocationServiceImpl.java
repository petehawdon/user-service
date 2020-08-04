package uk.co.hawdon.demo.users.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicLine;
import net.sf.geographiclib.GeodesicMask;
import net.sf.geographiclib.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

  /**
   * Number of metres in 1 mile as defined at https://en.wikipedia.org/wiki/Mile 
   */
  private static final double METRES_PER_MILE = 1609.344;

  /**
   * The distance in miles that defines proximity.
   */
  private Double proximityDistance;

  /**
   * The location of London was retrieved from https://www.latlong.net/place/london-the-uk-14153.html
   */
  private Map<String, Pair> cityLocations = new ConcurrentHashMap<>(); 
  {
    cityLocations.put("London", new Pair(51.509865, -0.118092));
  }
  
  public LocationServiceImpl(@Value("${proximity.distance.miles}") Double proximityDistance) {
    this.proximityDistance = proximityDistance;
  }

  /**
   * Determines whether a point is in proximity to a city.
   *
   * @param latitude point's latitude
   * @param longitude point's longitude
   * @return true if the distance is not greater than the number of miles defined by proximityDistance.
   */
  @Override
  public boolean isInProximity(final String cityName, final double latitude, final double longitude) {
    Pair cityLocation = cityLocations.get(cityName);
    return cityLocation != null && 
        milesBetween(cityLocation.first, cityLocation.second, latitude, longitude) <= proximityDistance;
  }
  
  /**
   * Code derived from https://geolake.com/code/distance.
   * 
   * Get the distance between two points in meters. 
   * @param latitude1 First point's latitude
   * @param longitude1 First point's longitude
   * @param latitude2 Second point's latitude
   * @param longitude2 Second point's longitude
   * @return Distance between the first and the second point in meters
   */
  private double milesBetween(final double latitude1, final double longitude1,
      final double latitude2, final double longitude2) {
    final GeodesicLine line = Geodesic.WGS84.InverseLine(latitude1, longitude1, latitude2, longitude2,
        GeodesicMask.DISTANCE_IN | GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE);
    return line.Distance() / METRES_PER_MILE;
  }
}
