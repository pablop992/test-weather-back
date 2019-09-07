package com.test.testweatherback.client.response.darksky;

import lombok.Data;

@Data
public class DarkSkyForecastItem {

  private Long time;
  private String summary;
  private String icon;
  private Double temperatureLow;
  private Double temperatureHigh;
}
