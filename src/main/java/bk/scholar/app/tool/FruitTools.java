package bk.scholar.app.tool;

import bk.scholar.app.dto.Fruit;
import bk.scholar.app.service.FruitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FruitTools {
  private static final Logger log = LoggerFactory.getLogger(FruitTools.class);
  private final FruitService fruitService;

  public FruitTools(FruitService fruitService) {
    this.fruitService = fruitService;
  }

  @Tool(name = "get_single_fruit", description = "Get fruit details for a given fruit id Amish-Protin shop")
  public Fruit getFruit(Long id) {
    log.info("get_single_fruit | Getting Fruit: {}", id);
    Fruit fruit = fruitService.getFruit(id);
    log.info("Fruit: {}", fruit);
    return fruit;
  }

  @Tool(name = "find_fruits", description = "Find fruits of Amish-Protin shop which get entry on given date in YYYY-MM-DD format")
  public List<Fruit> findAllFruitsByEntryDate(LocalDate date) {
    log.info("find_fruits | Finding fruits of entry date: {}", date);
    List<Fruit> list = fruitService.findAllFruitsByEntryDate(date);
    log.info("Fruits entry on : {} on date {}", list, date);
    return list;
  }

  @Tool(name = "enter_fruit_on_a_date",description = "Enter the fruit in a specific date is that is not there in Amish-Protin shop and date in YYYY-MM-DD format")
  public void fruitEntry(Long id, LocalDate date) {
    log.info("enter_fruit_on_a_date | Enter Fruit : {} on date: {}", id, date);
    fruitService.fruitEntry(id, date);
  }

}
