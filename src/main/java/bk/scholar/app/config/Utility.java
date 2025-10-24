package bk.scholar.app.config;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Utility {


  public static final Object APPLICATION_NAME = "APPLICATION_NAME";
  public static final Object TOKEN_PATHNAME = "TOKEN_PATHNAME";
  public static final Object REDIRECT_LOCALHOST = "REDIRECT_LOCALHOST";
  public static final Object TOKEN_URI = "TOKEN_URI";
  public static final Object AUTH_URI = "AUTH_URI";
  public static final Object CLIENT_SECRET = "CLIENT_SECRET";
  public static final Object CLIENT_ID = "CLIENT_ID";

  /**
   * Loads properties from a file in the resources folder.
   *
   * @param fileName the name of the file in the resources folder
   * @return a Properties object containing the key-value pairs from the file
   * @throws IOException if an error occurs while reading the file
   */
  public static Properties loadProperties(String fileName) throws IOException {
    // Create a Properties object to hold the properties
    Properties properties = new Properties();

    // Use the class loader to get the resource as a stream
    try (InputStream inputStream = Utility.class.getClassLoader().getResourceAsStream(fileName)) {
      if (inputStream == null) {
        throw new IOException("File not found in resources: " + fileName);
      }
      // Load the properties from the input stream
      properties.load(inputStream);
    }

    return properties;
  }

  /**
   * Loads properties from a given file name located in the resources folder.
   *
   * @param file the file object (the file name is used to look in the resources folder)
   * @return a Properties object containing the key-value pairs from the file
   * @throws IOException if an error occurs while reading the file
   */
  public static Properties loadProperties(File file) throws IOException {
    // Create a Properties object to store the key-value pairs
    Properties properties = new Properties();

    // Use the class loader to find the file in the resources folder
    try (InputStream inputStream = Utility.class.getClassLoader()
        .getResourceAsStream(file.getName())) {
      if (inputStream == null) {
        throw new IOException("File not found in resources: " + file.getName());
      }

      // Load the properties from the input stream
      properties.load(inputStream);
    }

    return properties;
  }

  // Main method for testing the Utility class
  public static void main(String[] args) {
    try {
      // Load the properties file located in the resources folder
      Properties propertiesJustFileName = Utility.loadProperties("Bandung.txt");
      // Print the loaded properties to the console
      /*propertiesJustFileName.forEach((key, value) ->
          System.out.println("propertiesJustFileName -> " + key + "=" + value)
      );*/
      //System.out.println("");
      // Load the properties using the Utility class
      Properties propertiesParamNewFile = Utility.loadProperties(new File("Bandung.txt"));
      // Print the loaded properties to the console
      /*propertiesParamNewFile.forEach((key, value) ->
          System.out.println("propertiesParamNewFile -> " + key + "=" + value)
      );*/

    } catch (IOException e) {
      // Handle exceptions (e.g., file not found, I/O errors)
      //System.err.println("Error loading properties: " + e.getMessage());
    }
  }

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
