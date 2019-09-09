package com.test.testweatherback.service.impl;

import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_COLLECTION;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_FORECAST_CLASS_KEY;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_FORECAST_CLASS_VALUE;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_FORECAST_CONTENT_KEY;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_FORECAST_ID_KEY;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_HOST;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.test.testweatherback.client.response.darksky.DarkSkyResponse;
import com.test.testweatherback.config.SpringTestConfig;
import com.test.testweatherback.dto.Forecast;
import com.test.testweatherback.dto.request.ForecastRequest;
import com.test.testweatherback.entity.ForecastWrapper;
import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import com.test.testweatherback.exception.ForecastNotFoundException;
import com.test.testweatherback.repository.ForecastRepository;
import com.test.testweatherback.test.util.AssertUtils;
import com.test.testweatherback.util.ForecastUtil;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
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
public class ForecastServiceImplTest {

  private MongodExecutable mongodExe;
  private MongodProcess mongod;

  private MockRestServiceServer mockServer;
  private ObjectMapper objMap = new ObjectMapper();

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ForecastServiceImpl target;

  @Autowired
  private ForecastRepository mongoRepo;

  @Before
  public void beforeEach() throws Exception {
    MongodStarter starter = MongodStarter.getDefaultInstance();
    IMongodConfig mongodConfig = new MongodConfigBuilder()
        .version(Version.Main.PRODUCTION)
        .net(new Net(MONGODB_HOST, MONGODB_PORT, Network.localhostIsIPv6()))
        .build();
    this.mongodExe = starter.prepare(mongodConfig);
    this.mongod = mongodExe.start();

    this.mockServer = MockRestServiceServer.bindTo(restTemplate).build();
  }

  @After
  public void afterEach() throws Exception {
    if (this.mongod != null) {
      this.mongod.stop();
      this.mongodExe.stop();
    }
  }

  @Test
  public void givenForecastRequest_WhenApiServesOK_ThenResponseIsReturned()
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

    Forecast response = target.getForecast(input);

    AssertUtils.assertForecast(response);

  }

  @Test
  public void givenForecastRequest_WhenApiHasError_ThenFallbackIsActivated()
      throws IOException, URISyntaxException {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.DARKSKY, City.BOGOTA, TemperatureUnit.CELSIUS);

    InputStream resource = new ClassPathResource(
        "mock/forecast/forecast-ok.response.json").getInputStream();

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8080/darksky/forecast")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    ForecastWrapper wrapper = new ForecastWrapper(
        ForecastUtil.getForecastIdForToday(input.getSource(), input.getCity(), input.getUnit()),
        objMap.readValue(resource, Forecast.class));

    mongoRepo.save(wrapper);

    Forecast response = target.getForecast(input);

    AssertUtils.assertForecast(response);

  }

  @Test(expected = ForecastNotFoundException.class)
  public void givenForecastRequest_WhenApiHasErrorAndFallbackDoesntHaveResponse_ThenExceptionIsThrown()
      throws IOException, URISyntaxException {

    ForecastRequest input = new ForecastRequest(
        ForecastSource.DARKSKY, City.BOGOTA, TemperatureUnit.CELSIUS);

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8080/darksky/forecast")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    Forecast response = target.getForecast(input);

    AssertUtils.assertForecast(response);

  }

}
