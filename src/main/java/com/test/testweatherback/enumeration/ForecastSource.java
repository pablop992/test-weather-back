package com.test.testweatherback.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ForecastSource {

  DARKSKY(1),
  APIXU(2),
  //Only for test purposes, do not use it!!
  UNKNOWN(0);

  private Integer id;

}
