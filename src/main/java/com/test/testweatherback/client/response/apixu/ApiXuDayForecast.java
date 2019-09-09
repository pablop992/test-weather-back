package com.test.testweatherback.client.response.apixu;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiXuDayForecast {

  @JsonProperty("date_epoch")
  @NotNull
  private Long dateEpoch;

  @NotNull
  @Valid
  private ApiXuDay day;
}
