package bk.scholar.app;

import bk.scholar.app.service.StudentService;
import bk.scholar.app.tool.GSuiteTools;
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
      GSuiteTools gSuiteTools,
      StudentService studentService) {
    return List.of(
        ToolCallbacks.from(
            gSuiteTools,
            studentService
        ));
  }

}
