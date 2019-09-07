package com.test.testweatherback.client;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.exception.ForecastNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Slf4j
public abstract class GenericForecastClient<Rq, Rs, Out> {

  private final RestTemplate restTemplate;

  protected HttpMethod method;
  protected String url;
  protected MultiValueMap<String, String> headers;
  protected MultiValueMap<String, String> queryParams;
  protected Map<String, String> pathParams;

  protected GenericForecastClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Out executeHttpRequest(Rq input) {
    HttpHeaders headers = new HttpHeaders();

    HttpEntity<?> entity = new HttpEntity<>(this.headers);

    Rs response;
    try {
      response = (Rs) restTemplate.exchange(
          buildUrl(input),
          this.method,
          entity,
          getTypeReference()).getBody();
    } catch (RestClientException rce) {
      rce.printStackTrace();
      throw new ForecastNotFoundException();
    }

    log.info("Sucessfull Forecast retrieve");

    return mapToResponse(response, input);
  }

  private String buildUrl(Rq input) {

    MultiValueMap<String, String> query = new LinkedMultiValueMap<>();

    if (Objects.nonNull(this.queryParams)) {
      query.addAll(this.queryParams);
    }

    query.addAll(getQueryParams(input));

    Map<String, String> path = new HashMap<>();

    if (Objects.nonNull(this.pathParams)) {
      path.putAll(this.pathParams);
    }

    path.putAll(getPathParams(input));

    return UriComponentsBuilder.fromHttpUrl(url).queryParams(query)
        .buildAndExpand(path).toUriString();
  }

  public abstract MultiValueMap<String, String> getQueryParams(Rq input);

  public abstract Map<String, String> getPathParams(Rq input);

  public abstract ParameterizedTypeReference<Rs> getTypeReference();

  public abstract Out mapToResponse(Rs response, Rq request);

  public abstract ForecastSource getSource();

}
