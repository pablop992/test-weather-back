package com.test.testweatherback.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.test.testweatherback")
public class SpringTestConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
