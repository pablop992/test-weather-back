package com.test.testweatherback.client.response.darksky;

import java.util.List;
import lombok.Data;

@Data
public class DarkSkyDailyForecast {

  private List<DarkSkyForecastItem> data;
}
