package com.test.testweatherback.client;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.test.testweatherback.config.SpringTestConfig;
import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import com.test.testweatherback.exception.EmptyForecastException;
import com.test.testweatherback.exception.ForecastNotFoundException;
import com.test.testweatherback.exception.InvalidForecastResponseException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = SpringTestConfig.class)
@ActiveProfiles("test")
public class DarkSkyClientTest {

  @Autowired
  private DarkSkyClient target;

  @Autowired
  private RestTemplate restTemplate;

  private MockRestServiceServer mockServer;

  @Before
  public void init() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void givenDarkSkyForecastRequest_WhenServiceResponseIsOk_ThenResponseIsSuccessful()
      throws IOException, URISyntaxException {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.DARKSKY, City.BOGOTA, TemperatureUnit.CELSIUS);

    InputStream resource = new ClassPathResource(
        "mock/darksky/darksky-ok.response.json").getInputStream();

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8080/darksky/forecast")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(IOUtils.toString(resource, Charset.forName("UTF-8"))));

    Forecast response = target.executeHttpRequest(input);

    assert(Objects.nonNull(response));
    assert(Objects.nonNull(response.getTodayForecast()));
    assert(Objects.nonNull(response.getNextDaysForecast()));
    assert(!response.getNextDaysForecast().isEmpty());

  }

  @Test(expected = ForecastNotFoundException.class)
  public void givenDarkSkyForecastRequest_WhenServiceResponseIsError_ThenExceptionIsRaised()
      throws IOException, URISyntaxException {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.DARKSKY, City.BOGOTA, TemperatureUnit.CELSIUS);

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8080/darksky/forecast")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    Forecast response = target.executeHttpRequest(input);

  }

  @Test(expected = InvalidForecastResponseException.class)
  public void givenDarkSkyForecastRequest_WhenServiceResponseIsEmpty_ThenExceptionIsRaised()
      throws IOException, URISyntaxException {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.DARKSKY, City.BOGOTA, TemperatureUnit.CELSIUS);

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8080/darksky/forecast")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("{}"));

    Forecast response = target.executeHttpRequest(input);

  }

}
