package com.test.testweatherback.service;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;

public interface ForecastService {

  Forecast getForecast(ForecastRequest request);

}
