package com.test.testweatherback.client;

import com.test.testweatherback.client.response.apixu.ApiXuResponse;
import com.test.testweatherback.client.response.darksky.DarkSkyResponse;
import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.misc.GeographicLocation;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.mapper.ApiXuForecastMapper;
import com.test.testweatherback.mapper.GenericForecastMapper;
import com.test.testweatherback.util.ApplicationConstants;
import com.test.testweatherback.util.LocationTranslator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties(prefix = "weather.api.apixu")
public class ApiXuClient extends GenericForecastClient<ForecastRequest, ApiXuResponse, Forecast> {

  private final GenericForecastMapper mapper;
  private final LocationTranslator locationTranslator;
  private ParameterizedTypeReference typeRef = new ParameterizedTypeReference<ApiXuResponse>() {
  };

  public ApiXuClient(RestTemplate restTemplate,
      @Qualifier("apiXuMapper") GenericForecastMapper mapper,
      LocationTranslator locationTranslator) {
    super(restTemplate);
    this.mapper = mapper;
    this.locationTranslator = locationTranslator;
  }

  @Override
  public MultiValueMap<String, String> getQueryParams(ForecastRequest input) {

    GeographicLocation location = locationTranslator.getLocation(input.getCity());

    return new LinkedMultiValueMap<String, String>() {{
      put("q", Collections.singletonList(
          String.format(ApplicationConstants.COORDINATES_PAIR_FORMAT, location.getLatitude(),
              location.getLongitude())));
    }};
  }

  @Override
  public Map<String, String> getPathParams(ForecastRequest input) {
    return Collections.emptyMap();
  }

  @Override
  public ParameterizedTypeReference<ApiXuResponse> getTypeReference() {
    return this.typeRef;
  }

  @Override
  public Forecast mapToResponse(ApiXuResponse response, ForecastRequest request) {
    return (Forecast) mapper.mapSourceResponse(response, request.getUnit());
  }

  @Override
  public ForecastSource getSource() {
    return ForecastSource.APIXU;
  }
}
