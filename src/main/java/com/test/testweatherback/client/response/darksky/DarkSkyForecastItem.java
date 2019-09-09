package com.test.testweatherback.client.response.darksky;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DarkSkyForecastItem {

  @NotNull
  private Long time;
  private String summary;
  @NotNull
  private String icon;
  @NotNull
  private Double temperatureLow;
  @NotNull
  private Double temperatureHigh;
}
