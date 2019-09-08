package com.test.testweatherback.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "api.web")
@Setter
public class RestConfig {

  private String keystore;
  private String keystorePassword;

  @Bean
  public RestTemplate restTemplate()
      throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException,
      KeyStoreException, KeyManagementException {
    TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
    SSLContext sslContext = SSLContextBuilder
        .create()
        .loadKeyMaterial(ResourceUtils.getFile(this.keystore),
            this.keystorePassword.toCharArray(), this.keystorePassword.toCharArray())

        .loadTrustMaterial(null, acceptingTrustStrategy)
        .build();
    HttpClient client = HttpClients.custom()
        .setSSLContext(sslContext)
        .build();
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(client);
    RestTemplate restTemplate = new RestTemplate(requestFactory);
    return restTemplate;
  }

}
