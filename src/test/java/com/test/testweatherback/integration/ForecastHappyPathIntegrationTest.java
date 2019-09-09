package com.test.testweatherback.integration;

import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_HOST;
import static com.test.testweatherback.test.util.EmbeddedMongoDbConstants.MONGODB_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.testweatherback.config.SpringTestConfig;
import com.test.testweatherback.controller.ForecastController;
import com.test.testweatherback.enumeration.City;
import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.TemperatureUnit;
import com.test.testweatherback.repository.ForecastRepository;
import com.test.testweatherback.util.ResourceConstants;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = SpringTestConfig.class)
@ActiveProfiles("test")
public class ForecastHappyPathIntegrationTest {

  private MongodExecutable mongodExe;
  private MongodProcess mongod;

  private MockRestServiceServer mockServer;
  private ObjectMapper objMap = new ObjectMapper();

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private MockMvc mockMvc;

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
  public void testIntegrationHappyPath() throws Exception {

    InputStream resource = new ClassPathResource(
        "mock/darksky/darksky-ok.response.json").getInputStream();

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8080/darksky/forecast")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(IOUtils.toString(resource, Charset.forName("UTF-8"))));

    this.mockMvc
        .perform(get(ResourceConstants.FORECAST_RESOURCE)
            .param(ResourceConstants.SOURCE_VARIABLE_NAME, ForecastSource.DARKSKY.name())
            .param(ResourceConstants.CITY_VARIABLE_NAME, City.BOGOTA.name())
            .param(ResourceConstants.UNIT_VARIABLE_NAME, TemperatureUnit.CELSIUS.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.todayForecast").exists())
        .andExpect(jsonPath("$.nextDaysForecast").exists());

  }


}
