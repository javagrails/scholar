package bk.scholar.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Value("${openweather.api.url}")
  private String weatherApiUrl;

  @Bean
  public RestClient weatherRestClient() {
    return RestClient.builder()
        .baseUrl(weatherApiUrl)
        .defaultHeader("Content-Type", "application/json")
        .build();
  }
}
