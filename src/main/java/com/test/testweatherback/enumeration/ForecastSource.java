package com.test.testweatherback.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ForecastSource {

  DARKSKY(1),
  APIXU(2);

  private Integer id;

}
