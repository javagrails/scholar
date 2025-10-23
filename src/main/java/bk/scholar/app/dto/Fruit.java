package bk.scholar.app.dto;

public class Fruit {
  private Long id;
  private String title;
  private Float price;

  public Fruit(){}

  public Fruit(Long id, String title, Float price) {
    this.id = id;
    this.title = title;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Fruit{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", price=" + price +
        '}';
  }
}

//1	Apple	409.90
//2	Mango	199.90
//3	Jack Fruit	301.90
