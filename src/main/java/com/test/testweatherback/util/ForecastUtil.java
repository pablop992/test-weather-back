package com.test.testweatherback.util;

import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ForecastUtil {

  private static final String FORECAST_ID_DF = "ddMMyyyy";
  private static final DateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.ENGLISH);
  private static final String FORECAST_ID_SEPARATOR = "-";
  private static final Long EPOCH_TO_MILLIS_MULTIPLICATOR = 1000l;

  public static String getForecastIdForToday(ForecastSource source, City city,
      TemperatureUnit unit) {

    StringBuilder builder = new StringBuilder();

    builder.append(source.getId());
    builder.append(FORECAST_ID_SEPARATOR);
    builder.append(city.getId());
    builder.append(FORECAST_ID_SEPARATOR);
    builder.append(unit.getId());
    builder.append(FORECAST_ID_SEPARATOR);
    builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern(FORECAST_ID_DF)));

    return builder.toString();

  }

  public static String extractDayOfWeekFromEpochTime(Long time) {
    String day = DAY_OF_WEEK_FORMAT.format(new Date(time * EPOCH_TO_MILLIS_MULTIPLICATOR));
    log.debug("Day to print: {}", day);

    return day;
  }

  public static Double fahrenheitToCelsius(Double temperature) {
    return ((temperature - 32.0) * 5.0) / 9;
  }

}
