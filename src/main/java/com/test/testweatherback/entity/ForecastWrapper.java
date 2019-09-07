package com.test.testweatherback.entity;

import com.test.testweatherback.dto.Forecast;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class ForecastWrapper {

  @Id
  private String id;

  private Forecast forecast;

}
