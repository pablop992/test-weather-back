package com.test.testweatherback.client;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.ForecastSource;
import java.util.Objects;
import lombok.Data;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public abstract class GenericForecastClient<Rq, Rs> {

  private final RestTemplate restTemplate;

  protected HttpMethod method;
  protected String url;
  protected MultiValueMap<String, String> headers;
  protected MultiValueMap<String, String> queryParams;
  protected MultiValueMap<String, Object> pathParams;

  protected GenericForecastClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Forecast executeHttpRequest(Rq input) {
    HttpHeaders headers = new HttpHeaders();

    HttpEntity<?> entity = new HttpEntity<>(this.headers);

    HttpEntity<String> response = restTemplate.exchange(
        buildUrl(input),
        this.method,
        entity,
        String.class);

    return new Forecast();
  }

  private String buildUrl(Rq input) {

    MultiValueMap<String, String> query = new LinkedMultiValueMap<>();

    if(Objects.nonNull(this.queryParams)) {
      query.addAll(this.queryParams);
    }

    query.addAll(getQueryParams(input));

    MultiValueMap<String, Object> path = new LinkedMultiValueMap<>();

    if(Objects.nonNull(this.pathParams)) {
      path.addAll(this.pathParams);
    }

    path.addAll(getPathParams(input));

    return UriComponentsBuilder.fromHttpUrl(url).queryParams(query)
        .buildAndExpand(path).toUriString();
  }

  public abstract MultiValueMap<String, String> getQueryParams(Rq input);

  public abstract MultiValueMap<String, Object> getPathParams(Rq input);

  public abstract Forecast mapToForecastObject(Rs response);

  public abstract ForecastSource getSource();

}
