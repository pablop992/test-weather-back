package com.test.testweatherback.mapper;

import com.test.testweatherback.enumeration.TemperatureUnit;

public interface GenericForecastMapper<In, Out> {

  Out mapSourceResponse(In source, TemperatureUnit unit);

}
