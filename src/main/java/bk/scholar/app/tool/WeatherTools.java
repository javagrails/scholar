package bk.scholar.app.tool;

import bk.scholar.app.dto.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherTools {

  private static final Logger log = LoggerFactory.getLogger(WeatherTools.class);
  private final RestClient weatherRestClient;
  private final String apiKey;

  public WeatherTools(RestClient weatherRestClient,
      @Value("${openweather.api.key}") String apiKey) {
    this.weatherRestClient = weatherRestClient;
    this.apiKey = apiKey;
  }

  @Tool(name = "bangladeshi_city_weather", description = "It will provide weather information's of Bangladeshi cities like Dhaka, Khulna, Tangail")
  public String getCurrentWeather(String city, String units) {
    log.info("bangladeshi_city_weather | Getting today's date");
    // Handle null/empty units
    if (units == null || units.trim().isEmpty()) {
      units = "metric";
    }

    String encodedCity = city.replace(" ", "+");
    String uri = UriComponentsBuilder.fromPath("/weather")
        .queryParam("appid", apiKey)
        .queryParam("q", encodedCity)
        .queryParam("units", units)
        .toUriString();

    try {
      WeatherResponse response = weatherRestClient
          .get()
          .uri(uri)
          .retrieve()
          .body(WeatherResponse.class);

      // Convert response to a descriptive string
      return formatWeatherResponse(response, units);

    } catch (Exception e) {
      return "Error fetching weather data: " + e.getMessage();
    }
  }

  private String formatWeatherResponse(WeatherResponse response, String units) {
    if (response == null || response.getMain() == null) {
      return "Unable to retrieve weather information.";
    }

    WeatherResponse.Main main = response.getMain();
    String description = (response.getWeather() != null && !response.getWeather().isEmpty())
        ? response.getWeather().get(0).getDescription()
        : "weather data";

    return String.format(
        "Current weather in %s, %s: %s. Temperature: %.1f°%s (feels like %.1f°%s). Humidity: %d%%. Pressure: %d hPa.",
        response.getName(),
        response.getSys() != null ? response.getSys().getCountry() : "Unknown",
        description,
        main.getTemp(),
        getUnitSymbol(units), // You'll need to track units or infer from temp range
        main.getTempMin(),
        getUnitSymbol(units),
        main.getHumidity(),
        main.getPressure()
    );
  }

  private String getUnitSymbol(String units) {
    if ("imperial".equalsIgnoreCase(units)) {
      return "F";
    }
    return "C"; // default to metric
  }
}
