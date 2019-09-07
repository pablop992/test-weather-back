package com.test.testweatherback.client;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.exception.ForecastSourceNotFound;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ForecastClientSelector {

  private final Map<ForecastSource, GenericForecastClient> clients;

  public ForecastClientSelector(List<GenericForecastClient> clients) {
    this.clients = clients.stream().collect(
        Collectors.toMap(GenericForecastClient::getSource, Function.identity()));
  }

  public Forecast getForecast(ForecastRequest request) {

    if(clients.containsKey(request.getSource())) {
      return clients.get(request.getSource()).executeHttpRequest(request);
    } else {
      throw new ForecastSourceNotFound();
    }
  }

}
