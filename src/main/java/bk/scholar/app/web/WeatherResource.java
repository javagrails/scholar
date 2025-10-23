package bk.scholar.app.web;

import bk.scholar.app.dto.WeatherResponse;
import bk.scholar.app.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WeatherResource {

  private final WeatherService weatherService;

  public WeatherResource(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @GetMapping("/weather")
  public ResponseEntity<WeatherResponse> getWeather(
      @RequestParam(defaultValue = "Dhaka") String city,
      @RequestParam(defaultValue = "metric") String units) {
    try {
      WeatherResponse response = weatherService.getWeather(city, units);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
