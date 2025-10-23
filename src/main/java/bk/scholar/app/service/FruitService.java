package bk.scholar.app.service;

import bk.scholar.app.dto.Fruit;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FruitService {

  private static final Map<Long, Fruit> fruitTable = new HashMap<>();
  private static final Map<LocalDate, List<Long>> fruitEntryTable = new HashMap<>();

  @PostConstruct
  void init() {
    //1	Apple	409.90
    //2	Mango	199.90
    //3	Jack Fruit	301.90

    fruitTable.put(1l, new Fruit(1l, "Apple", 409.90f));
    fruitTable.put(2l, new Fruit(2l, "Mango", 199.90f));
    fruitTable.put(3l, new Fruit(3l, "Jack Fruit", 301.90f));

    fruitEntryTable.put(LocalDate.now(), new ArrayList<>(List.of(1L, 3L)));
    fruitEntryTable.put(LocalDate.of(2025, 9, 2), new ArrayList<>(List.of(1l, 2l)));
    fruitEntryTable.put(LocalDate.of(2025, 8, 5), new ArrayList<>(List.of(2l, 3l)));
  }

  public Fruit getFruit(Long id) {
    return fruitTable.get(id);
  }

  public List<Fruit> findAllFruitsByEntryDate(LocalDate date) {
    List<Long> ids = fruitEntryTable.get(date);
    return ids == null ? List.of() : getFruits(ids);
  }

  public List<Fruit> getFruits(List<Long> ids) {
    return ids.stream().map(fruitTable::get).toList();
  }

  public void fruitEntry(Long id, LocalDate date) {
    List<Long> ids = fruitEntryTable.get(date);
    if (ids == null) {
      ids = List.of(id);
    } else {
      ids.add(id);
    }
    fruitEntryTable.put(date, ids);
    System.out.println("ddd");
  }
}
