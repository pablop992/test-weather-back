package com.test.testweatherback.client.response.apixu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiXuDay {

  @JsonProperty("maxtemp_c")
  private Double maxTempCelsius;

  @JsonProperty("maxtemp_f")
  private Double maxTempFahrenheit;

  @JsonProperty("mintemp_c")
  private Double minTempCelsius;

  @JsonProperty("mintemp_f")
  private Double minTempFahrenheit;

  private ApiXuCondition condition;

}
