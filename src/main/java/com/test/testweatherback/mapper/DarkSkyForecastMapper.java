package com.test.testweatherback.mapper;

import static com.test.testweatherback.util.ApplicationConstants.TEMPERATURE_FORMAT;

import com.test.testweatherback.client.response.darksky.DarkSkyForecastItem;
import com.test.testweatherback.client.response.darksky.DarkSkyResponse;
import com.test.testweatherback.dto.DayForecast;
import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import com.test.testweatherback.exception.EmptyForecastException;
import com.test.testweatherback.util.ForecastUtil;
import com.test.testweatherback.util.IconTranslator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class DarkSkyForecastMapper {

  private final IconTranslator iconTranslator;

  public DarkSkyForecastMapper(IconTranslator iconTranslator) {
    this.iconTranslator = iconTranslator;
  }

  public Forecast mapDarkSkyResponseToForecast(DarkSkyResponse source, TemperatureUnit unit) {

    if(Objects.isNull(source) || Objects.isNull(source.getDaily())
        || Objects.isNull(source.getDaily().getData())
        || source.getDaily().getData().isEmpty()) {
      throw new EmptyForecastException();
    }

    Forecast toReturn = new Forecast();
    toReturn.setTodayForecast(mapDayForecast(source.getDaily().getData().get(0), unit));

    List<DayForecast> nextDayForecast = new ArrayList<>(source.getDaily().getData().size() - 1);

    for (int i = 1; i < source.getDaily().getData().size(); i++) {
      nextDayForecast.add(mapDayForecast(source.getDaily().getData().get(i), unit));
    }

    toReturn.setNextDaysForecast(nextDayForecast);

    return toReturn;
  }

  public DayForecast mapDayForecast(DarkSkyForecastItem source, TemperatureUnit unit) {
    DayForecast dayForecast = new DayForecast();
    dayForecast.setDay(ForecastUtil.extractDayOfWeekFromEpochTime(source.getTime()));
    dayForecast.setIcon(iconTranslator.getIconId(ForecastSource.DARKSKY, source.getIcon()));
    dayForecast.setSummary(source.getSummary());

    Double max = source.getTemperatureHigh();
    Double min = source.getTemperatureLow();

    if (unit.equals(TemperatureUnit.CELSIUS)) {
      max = ForecastUtil.fahrenheitToCelsius(source.getTemperatureHigh());
      min = ForecastUtil.fahrenheitToCelsius(source.getTemperatureLow());
    }

    dayForecast.setMaxTemperature(TEMPERATURE_FORMAT.format(max));
    dayForecast.setMinTemperature(TEMPERATURE_FORMAT.format(min));

    return dayForecast;

  }

}
