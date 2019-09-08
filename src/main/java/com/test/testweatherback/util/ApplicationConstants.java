package com.test.testweatherback.util;

import java.text.DecimalFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

  public static DecimalFormat TEMPERATURE_FORMAT = new DecimalFormat("#.##");
  public static String COORDINATES_PAIR_FORMAT = "%s,%s";

}
