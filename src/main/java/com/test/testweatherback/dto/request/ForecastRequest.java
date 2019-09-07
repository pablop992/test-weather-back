package com.test.testweatherback.dto.request;

import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForecastRequest {

  private ForecastSource source;
  private City city;
  private TemperatureUnit unit;

}
