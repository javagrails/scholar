package bk.scholar.app;

import bk.scholar.app.service.StudentService;
import bk.scholar.app.tool.FruitTools;
import bk.scholar.app.tool.GSuiteTools;
import bk.scholar.app.tool.UtilityTools;
import bk.scholar.app.tool.WeatherTools;
import java.util.List;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScholarApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScholarApplication.class, args);
  }

  @Bean
  public List<ToolCallback> toolCallbacks(
      UtilityTools utilityTools,
      FruitTools fruitTools,
      GSuiteTools gSuiteTools,
      WeatherTools weatherTools,
      StudentService studentService) {
    return List.of(
        ToolCallbacks.from(
            utilityTools,
            fruitTools,
            gSuiteTools,
            weatherTools,
            studentService
        ));
  }

}
