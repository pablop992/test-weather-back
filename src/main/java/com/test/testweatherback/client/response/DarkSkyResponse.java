package com.test.testweatherback.client.response;

import java.util.List;
import lombok.Data;

@Data
public class DarkSkyResponse {

  private DarkSkyDailyForecast daily;

  @Data
  public class DarkSkyDailyForecast {
    private List<DarkSkyForecastItem> data;
  }

  @Data
  public class DarkSkyForecastItem {
    private Long time;
    private String summary;
    private String icon;
    private Double temperatureLow;
    private Double temperatureHigh;
  }

}
