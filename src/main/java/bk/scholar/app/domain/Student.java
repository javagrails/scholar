package bk.scholar.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "student")
public class Student implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "email", length = 150, nullable = false, unique = true)
  private String email;

  @Column(name = "gender", length = 10, nullable = false)
  private String gender;

  @Column(name = "city", length = 500, nullable = false)
  private String city;

  @Column(name = "insertion_method", length = 50, nullable = false)
  private String method;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Student student = (Student) o;
    return Objects.equals(id, student.id) && Objects.equals(name, student.name)
        && Objects.equals(email, student.email) && Objects.equals(gender,
        student.gender) && Objects.equals(city, student.city) && Objects.equals(
        method, student.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, gender, city, method);
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", gender='" + gender + '\'' +
        ", city='" + city + '\'' +
        ", method='" + method + '\'' +
        '}';
  }
}
