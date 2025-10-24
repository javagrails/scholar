package bk.scholar.app.tool;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class UtilityTools {

  private static final Logger log = LoggerFactory.getLogger(UtilityTools.class);

  @Tool(name = "today_date", description = "Get the today's date in the format yyyy-MM-dd")
  public String getTodayDate() {
    log.info("today_date | Getting today's date");
    return LocalDate.now().toString();
  }

  @Tool(name = "personal_information", description = "Get personal information. The 'fields' parameter MUST be a comma-separated list of field names. " +
      "Valid fields: name, phone, city, email, occupation, country. " +
      "Example: 'name,phone' or 'city,email,occupation'")
  public String getPersonal(@ToolParam String fields) {
    log.info("my_personal | Getting personal info, fields: {}", fields);
    try {
      // Parse comma-separated fields
      String[] requestedFields = fields.split(",");
      log.info("my_personal | Getting personal info, requestedFields: {}", requestedFields);
      Map<String, String> result = new HashMap<>();

      for (String field : requestedFields) {
        String cleanField = field.trim().toLowerCase();
        // Find matching key (case-insensitive)
        String matchedKey = findMatchingKey(cleanField);
        if (matchedKey != null) {
          result.put(matchedKey, personalInfo.get(matchedKey));
        }
      }

      if (result.isEmpty()) {
        return "No matching personal information found for the requested fields.";
      }

      // Format as natural language response
      return formatResponse(result);

    } catch (Exception e) {
      return "Error retrieving personal information: " + e.getMessage();
    }
  }

  private final Map<String, String> personalInfo = Map.of(
      "name", "M. M. SALEH {sAlMaN}",
      "phone", "+88-01111111114",
      "city", "Dhaka",
      "email", "salman@example.com",
      "occupation", "Software Engineer, taking online course on core-JAVA home and gulf area and many more",
      "country", "Bangladesh"
  );
  private static final Map<String, String> FIELD_MAPPINGS = createFieldMappings();

  private static Map<String, String> createFieldMappings() {
    Map<String, String> mappings = new HashMap<>();
    mappings.put("name", "name");
    mappings.put("full name", "name");
    mappings.put("phone", "phone");
    mappings.put("phone number", "phone");
    mappings.put("telephone", "phone");
    mappings.put("mobile", "phone");
    mappings.put("city", "city");
    mappings.put("location", "city");
    mappings.put("email", "email");
    mappings.put("email address", "email");
    mappings.put("occupation", "occupation");
    mappings.put("job", "occupation");
    mappings.put("country", "country");
    return Collections.unmodifiableMap(mappings);
  }

  private String findMatchingKey(String requestedField) {
    if (personalInfo.containsKey(requestedField)) {
      return requestedField;
    }
    return FIELD_MAPPINGS.get(requestedField);
  }

  private String formatResponse(Map<String, String> info) {
    if (info.size() == 1) {
      String key = info.keySet().iterator().next();
      String value = info.get(key);
      return String.format("Your %s is %s.", formatFieldName(key), value);
    } else {
      StringBuilder response = new StringBuilder("Here is your requested information:\n");
      for (Map.Entry<String, String> entry : info.entrySet()) {
        response.append(String.format("- %s: %s\n",
            capitalizeFirst(formatFieldName(entry.getKey())),
            entry.getValue()));
      }
      return response.toString().trim();
    }
  }

  private String formatFieldName(String key) {
    switch (key) {
      case "name": return "name";
      case "phone": return "phone number";
      case "city": return "city";
      case "email": return "email address";
      case "occupation": return "occupation";
      case "country": return "country";
      default: return key;
    }
  }

  private String capitalizeFirst(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

}
