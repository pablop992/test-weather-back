package com.test.testweatherback.client.response.apixu;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class ApiXuForecast {

  @JsonProperty("forecastday")
  private List<ApiXuDayForecast> forecastDay;

}
