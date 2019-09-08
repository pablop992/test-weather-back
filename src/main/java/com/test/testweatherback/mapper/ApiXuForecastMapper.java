package com.test.testweatherback.mapper;

import static com.test.testweatherback.util.ApplicationConstants.TEMPERATURE_FORMAT;

import com.test.testweatherback.client.response.apixu.ApiXuDayForecast;
import com.test.testweatherback.client.response.apixu.ApiXuForecast;
import com.test.testweatherback.client.response.apixu.ApiXuResponse;
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
public class ApiXuForecastMapper {

  private final IconTranslator iconTranslator;

  public ApiXuForecastMapper(IconTranslator iconTranslator) {
    this.iconTranslator = iconTranslator;
  }

  public Forecast mapApiXuResponseToForecast(ApiXuResponse source, TemperatureUnit unit) {

    if(Objects.isNull(source) || Objects.isNull(source.getForecast())
        || Objects.isNull(source.getForecast().getForecastDay())
        || source.getForecast().getForecastDay().isEmpty()) {
      throw new EmptyForecastException();
    }

    Forecast toReturn = new Forecast();


    toReturn.setTodayForecast(mapDayForecast(source.getForecast().getForecastDay().get(0), unit));

    List<DayForecast> nextDayForecast = new ArrayList<>(source.getForecast().getForecastDay().size() - 1);

    for (int i = 1; i < source.getForecast().getForecastDay().size(); i++) {
      nextDayForecast.add(mapDayForecast(source.getForecast().getForecastDay().get(i), unit));
    }

    toReturn.setNextDaysForecast(nextDayForecast);

    return toReturn;
  }

  public DayForecast mapDayForecast(ApiXuDayForecast source, TemperatureUnit unit) {
    DayForecast dayForecast = new DayForecast();
    dayForecast.setDay(ForecastUtil.extractDayOfWeekFromEpochTime(source.getDateEpoch()));
    dayForecast.setIcon(iconTranslator.getIconId(ForecastSource.APIXU, source.getDay().getCondition().getCode()));
    dayForecast.setSummary(source.getDay().getCondition().getText());

    double max = 0.0d;
    Double min = 0.0d;

    switch(unit) {
      case CELSIUS:
        max = source.getDay().getMaxTempCelsius();
        min = source.getDay().getMinTempCelsius();
        break;
      case FAHRENHEIT:
        max = source.getDay().getMaxTempFahrenheit();
        min = source.getDay().getMinTempFahrenheit();
        break;
    }

    dayForecast.setMaxTemperature(TEMPERATURE_FORMAT.format(max));
    dayForecast.setMinTemperature(TEMPERATURE_FORMAT.format(min));

    return dayForecast;

  }

}
