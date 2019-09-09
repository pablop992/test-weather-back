package com.test.testweatherback.test.util;

import com.test.testweatherback.dto.DayForecast;
import com.test.testweatherback.dto.Forecast;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class AssertUtils {

  public static void assertForecast(Forecast input) {
    assert(Objects.nonNull(input));
    AssertUtils.assertDayForecastFields(input.getTodayForecast());

    assert(Objects.nonNull(input.getNextDaysForecast()));

    for(DayForecast day : input.getNextDaysForecast()) {
      assertDayForecastFields(day);
    }
  }

  public static void assertDayForecastFields(DayForecast forecast) {
    assert(Objects.nonNull(forecast));
    assert(StringUtils.isNotEmpty(forecast.getDay()));
    assert(StringUtils.isNotEmpty(forecast.getSummary()));
    assert(StringUtils.isNotEmpty(forecast.getIcon()));
    assert(StringUtils.isNotEmpty(forecast.getMaxTemperature()));
    assert(StringUtils.isNotEmpty(forecast.getMinTemperature()));
  }

}
