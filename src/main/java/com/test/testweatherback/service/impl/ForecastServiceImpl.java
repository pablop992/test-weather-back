package com.test.testweatherback.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.test.testweatherback.client.ForecastClientSelector;
import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.entity.ForecastWrapper;
import com.test.testweatherback.exception.ForecastNotFoundException;
import com.test.testweatherback.repository.ForecastRepository;
import com.test.testweatherback.service.ForecastService;
import com.test.testweatherback.util.ForecastUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForecastServiceImpl implements ForecastService {

  private final ForecastClientSelector selector;
  private final ForecastRepository repository;


  public ForecastServiceImpl(ForecastClientSelector selector, ForecastRepository repository) {
    this.selector = selector;
    this.repository = repository;
  }

  @Override
  @HystrixCommand(fallbackMethod = "getForecastFallback", commandProperties = {
      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
  })
  public Forecast getForecast(ForecastRequest request) {
    Forecast toReturn = selector.getForecast(request);
    saveForecast(toReturn, request);
    return toReturn;
  }

  private Forecast getForecastFallback(ForecastRequest request) {

    log.debug("Passing request to Fallback...");

    return repository.findById(
        ForecastUtil
            .getForecastIdForToday(request.getSource(), request.getCity(), request.getUnit()))
        .orElseThrow(ForecastNotFoundException::new).getForecast();
  }

  @Async
  void saveForecast(Forecast forecast, ForecastRequest request) {

    String databaseId = ForecastUtil
        .getForecastIdForToday(request.getSource(), request.getCity(), request.getUnit());

    if (!repository.findById(databaseId).isPresent()) {
      ForecastWrapper wrapper = new ForecastWrapper(databaseId, forecast);
      repository.insert(wrapper);
    }
  }


}
