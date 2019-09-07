package com.test.testweatherback.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum TemperatureUnit {

  CELSIUS("C"),
  FAHRENHEIT("F");

  private String id;
}
