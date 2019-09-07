package com.test.testweatherback.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum City {

  BOGOTA(1),
  SAO_PAULO(2),
  NEW_YORK(3);

  private Integer id;
}
