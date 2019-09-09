package com.test.testweatherback.client.response.apixu;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiXuCondition {

  private String text;
  @NotEmpty
  private String code;

}
