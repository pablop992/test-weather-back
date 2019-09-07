package com.test.testweatherback.client.response.apixu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiXuDayForecast {

  @JsonProperty("date_epoch")
  private Long dateEpoch;

  private ApiXuDay day;
}
