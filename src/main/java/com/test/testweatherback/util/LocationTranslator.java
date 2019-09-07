package com.test.testweatherback.util;

import com.test.testweatherback.dto.misc.GeographicLocation;
import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.exception.CoordinatesNotFoundException;
import java.util.EnumMap;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weather.location")
@Data
public class LocationTranslator {

  private EnumMap<City, GeographicLocation> coordinates;

  public GeographicLocation getLocation(City city) {

    if (this.coordinates.containsKey(city)) {
      return this.coordinates.get(city);
    } else {
      throw new CoordinatesNotFoundException();
    }
  }
}
