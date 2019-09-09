package com.test.testweatherback.handler;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.exception.CoordinatesNotFoundException;
import com.test.testweatherback.exception.EmptyForecastException;
import com.test.testweatherback.exception.ForecastNotFoundException;
import com.test.testweatherback.exception.ForecastSourceNotFound;
import com.test.testweatherback.exception.InvalidForecastResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class TestWeatherBackErrorHandler {

  @ExceptionHandler({ForecastNotFoundException.class, InvalidForecastResponseException.class})
  public ResponseEntity handleForecastNotFound() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler({CoordinatesNotFoundException.class, ForecastSourceNotFound.class})
  public ResponseEntity<String> handleStupidDeveloperExceptions() {
    return ResponseEntity.unprocessableEntity().body("Bad Parametrization. Please check.");
  }

}
