package com.test.testweatherback.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Icon {

  SUNNY("sunny"),
  CLOUDY("cloudy"),
  RAINY("rainy"),
  SNOWY("snowy"),
  WINDY("windy"),
  FOGGY("foggy"),
  THUNDERY("thundery"),
  UNKNOWN("unknown");

  private String value;
}
