package bk.scholar.app.config;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utility {

  public static Map<String, EventDateTime> createEventTime(String dateStr) {
    // Parse the input date (e.g., "2025-10-25")
    LocalDate date = LocalDate.parse(dateStr);

    // Define Dhaka timezone
    ZoneId dhakaZone = ZoneId.of("Asia/Dhaka");

    // Set start time to 2:00 PM on that date in Dhaka
    ZonedDateTime startTime = date.atTime(14, 0).atZone(dhakaZone);

    // Randomly choose duration: 1, 2, 3, or 4 hours
    int randomHours = new Random().nextInt(4) + 1;
    ZonedDateTime endTime = startTime.plusHours(randomHours);

    // Format as ISO 8601 strings with timezone offset (e.g., +06:00)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    String startStr = startTime.format(formatter);
    String endStr = endTime.format(formatter);

    // Build EventDateTime objects
    EventDateTime start = new EventDateTime()
        .setDateTime(new DateTime(startStr))
        .setTimeZone("Asia/Dhaka");

    EventDateTime end = new EventDateTime()
        .setDateTime(new DateTime(endStr))
        .setTimeZone("Asia/Dhaka");

    // Return as map
    Map<String, EventDateTime> result = new HashMap<>();
    result.put("start", start);
    result.put("end", end);
    return result;
  }

}
