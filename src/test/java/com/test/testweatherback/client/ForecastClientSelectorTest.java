package com.test.testweatherback.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import com.test.testweatherback.exception.ForecastSourceNotFound;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ForecastClientSelectorTest {

  private ForecastClientSelector target;

  @Mock
  private DarkSkyClient client;

  @Before
  public void setUp () {
    when(client.getSource()).thenCallRealMethod();
    when(client.executeHttpRequest(any(ForecastRequest.class))).thenReturn(new Forecast());

    target = new ForecastClientSelector(Collections.singletonList(client));
  }

  @Test
  public void givenForecastRequest_whenSourceHasClient_ThenReturnResponse() {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.DARKSKY, City.BOGOTA, TemperatureUnit.CELSIUS);

    target.getForecast(input);

  }

  @Test(expected = ForecastSourceNotFound.class)
  public void givenForecastRequest_whenSourceDoesntHaveClient_ThenReturnException() {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.UNKNOWN, City.BOGOTA, TemperatureUnit.CELSIUS);

    target.getForecast(input);

  }


}
