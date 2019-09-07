package com.test.testweatherback.dto;

import lombok.Data;

@Data
public class DayForecast {

  private String day;
  private String summary;
  private String minTemperature;
  private String maxTemperature;
  private String icon;

}
