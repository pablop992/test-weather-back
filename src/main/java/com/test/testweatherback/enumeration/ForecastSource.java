package com.test.testweatherback.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ForecastSource {

  DARKSKY(1),
  ACCUWEATHER(2),
  OPENWEATHERMAP(3);

  private Integer id;

}
