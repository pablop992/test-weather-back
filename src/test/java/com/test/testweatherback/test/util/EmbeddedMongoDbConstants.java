package com.test.testweatherback.test.util;

public class EmbeddedMongoDbConstants {

  public static final String MONGODB_HOST = "localhost";
  public static final Integer MONGODB_PORT = 27018;
  public static final String MONGODB_DATABASE = "forecast";
  public static final String MONGODB_COLLECTION = "forecastWrapper";

  public static final String MONGODB_FORECAST_ID_KEY = "_id";
  public static final String MONGODB_FORECAST_CONTENT_KEY = "forecast";
  public static final String MONGODB_FORECAST_CLASS_KEY = "_class";
  public static final String MONGODB_FORECAST_CLASS_VALUE = "com.test.testweatherback.entity.ForecastWrapper";

}
