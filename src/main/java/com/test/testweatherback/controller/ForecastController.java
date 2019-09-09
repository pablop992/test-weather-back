package com.test.testweatherback.controller;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import com.test.testweatherback.service.ForecastService;
import com.test.testweatherback.util.ResourceConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ResourceConstants.FORECAST_RESOURCE)
public class ForecastController {

  private final ForecastService forecastService;

  public ForecastController(ForecastService forecastService) {
    this.forecastService = forecastService;
  }

  @GetMapping
  public Forecast getForecast(@ModelAttribute("forecastRequest") ForecastRequest request) {
    return forecastService.getForecast(request);
  }

  @ModelAttribute()
  private ForecastRequest forecastRequest(
      @RequestParam(name = ResourceConstants.SOURCE_VARIABLE_NAME) ForecastSource source,
      @RequestParam(name = ResourceConstants.CITY_VARIABLE_NAME) City city,
      @RequestParam(name = ResourceConstants.UNIT_VARIABLE_NAME) TemperatureUnit unit) {
    return new ForecastRequest(source, city, unit);
  }

}
