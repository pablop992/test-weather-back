package com.test.testweatherback.util;

import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.tomcat.jni.Local;

public class ForecastUtil {

  private static final String FORECAST_ID_DF = "ddMMyyyy";
  private static final String FORECAST_ID_SEPARATOR = "-";

  public static String getForecastIdForToday(ForecastSource source, City city) {

    StringBuilder builder = new StringBuilder();

    builder.append(source.getId());
    builder.append(FORECAST_ID_SEPARATOR);
    builder.append(city.getId());
    builder.append(FORECAST_ID_SEPARATOR);
    builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern(FORECAST_ID_DF)));

    return builder.toString();

  }

}
