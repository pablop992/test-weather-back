package com.test.testweatherback.client;

import com.test.testweatherback.client.response.DarkSkyResponse;
import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.ForecastSource;
import java.util.LinkedHashMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties(prefix = "weather.api.dark-sky")
public class DarkSkyClient extends GenericForecastClient<ForecastRequest, DarkSkyResponse> {

  protected DarkSkyClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  public MultiValueMap<String, String> getQueryParams(ForecastRequest input) {
    return new LinkedMultiValueMap<>();
  }

  @Override
  public MultiValueMap<String, Object> getPathParams(ForecastRequest input) {
    return new LinkedMultiValueMap<>();
  }

  @Override
  public Forecast mapToForecastObject(DarkSkyResponse response) {
    return new Forecast();
  }

  @Override
  public ForecastSource getSource() {
    return ForecastSource.DARKSKY;
  }

}
