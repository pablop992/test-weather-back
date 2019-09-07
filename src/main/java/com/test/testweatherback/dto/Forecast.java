package com.test.testweatherback.dto;

import java.time.DayOfWeek;
import java.util.List;
import lombok.Data;

@Data
public class Forecast {

  private DayForecast todayForecast;
  private List<DayForecast> nextDaysForecast;

}
