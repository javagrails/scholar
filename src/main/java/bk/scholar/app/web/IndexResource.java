package bk.scholar.app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexResource {

  @GetMapping("/index")
  public String index() {
    return "Application { scholar } is running !!!!";
  }
}
