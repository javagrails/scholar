package bk.scholar.app.tool;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;

public class UtilityTools {

  private static final Logger log = LoggerFactory.getLogger(UtilityTools.class);

  @Tool(name = "today_date", description = "Get the today's date in the format yyyy-MM-dd")
  String getTodayDate() {
    log.info("Getting today's date");
    return LocalDate.now().toString();
  }
}
