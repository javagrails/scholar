package bk.scholar.app.service;

import bk.scholar.app.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

  private final RestClient weatherRestClient;
  private final String apiKey;

  public WeatherService(RestClient weatherRestClient,
      @Value("${openweather.api.key}") String apiKey) {
    this.weatherRestClient = weatherRestClient;
    this.apiKey = apiKey;
  }

  public WeatherResponse getWeather(String city, String units) {
    String encodedCity = city.replace(" ", "+");
    String uri = UriComponentsBuilder.fromPath("/weather")
        .queryParam("appid", apiKey)
        .queryParam("q", encodedCity)
        .queryParam("units", units)
        .toUriString();
    System.out.println("uri = " + uri.toString());

    try {
      final WeatherResponse body = weatherRestClient
          .get()
          .uri(uri)
          .retrieve()
          .body(WeatherResponse.class);
      return body;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /*public Double getTemperature() {
    String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
    RestClient

    RestTemplate restTemplate = new RestTemplate();
    WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

    return response != null ? response.getMain().getTemp() : null;
  }*/
}
