package bk.scholar.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
  @SerializedName("coord")
  Coord coord;

  @SerializedName("weather")
  List<Weather> weather;

  @SerializedName("base")
  String base;

  @SerializedName("main")
  Main main;

  @SerializedName("visibility")
  int visibility;

  @SerializedName("wind")
  Wind wind;

  @SerializedName("clouds")
  Clouds clouds;

  @SerializedName("dt")
  int dt;

  @SerializedName("sys")
  Sys sys;

  @SerializedName("id")
  int id;

  @SerializedName("name")
  String name;

  @SerializedName("cod")
  int cod;


  public void setCoord(Coord coord) {
    this.coord = coord;
  }
  public Coord getCoord() {
    return coord;
  }

  public void setWeather(List<Weather> weather) {
    this.weather = weather;
  }
  public List<Weather> getWeather() {
    return weather;
  }

  public void setBase(String base) {
    this.base = base;
  }
  public String getBase() {
    return base;
  }

  public void setMain(Main main) {
    this.main = main;
  }
  public Main getMain() {
    return main;
  }

  public void setVisibility(int visibility) {
    this.visibility = visibility;
  }
  public int getVisibility() {
    return visibility;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }
  public Wind getWind() {
    return wind;
  }

  public void setClouds(Clouds clouds) {
    this.clouds = clouds;
  }
  public Clouds getClouds() {
    return clouds;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }
  public int getDt() {
    return dt;
  }

  public void setSys(Sys sys) {
    this.sys = sys;
  }
  public Sys getSys() {
    return sys;
  }

  public void setId(int id) {
    this.id = id;
  }
  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }

  public void setCod(int cod) {
    this.cod = cod;
  }
  public int getCod() {
    return cod;
  }

  public static class Coord {

    @SerializedName("lon")
    double lon;

    @SerializedName("lat")
    double lat;


    public void setLon(double lon) {
      this.lon = lon;
    }
    public double getLon() {
      return lon;
    }

    public void setLat(double lat) {
      this.lat = lat;
    }
    public double getLat() {
      return lat;
    }

  }

  public static class Weather {

    @SerializedName("id")
    int id;

    @SerializedName("main")
    String main;

    @SerializedName("description")
    String description;

    @SerializedName("icon")
    String icon;


    public void setId(int id) {
      this.id = id;
    }
    public int getId() {
      return id;
    }

    public void setMain(String main) {
      this.main = main;
    }
    public String getMain() {
      return main;
    }

    public void setDescription(String description) {
      this.description = description;
    }
    public String getDescription() {
      return description;
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }
    public String getIcon() {
      return icon;
    }

  }

  public static class Main {

    @SerializedName("temp")
    double temp;

    @SerializedName("pressure")
    int pressure;

    @SerializedName("humidity")
    int humidity;

    @SerializedName("temp_min")
    double tempMin;

    @SerializedName("temp_max")
    double tempMax;


    public void setTemp(double temp) {
      this.temp = temp;
    }
    public double getTemp() {
      return temp;
    }

    public void setPressure(int pressure) {
      this.pressure = pressure;
    }
    public int getPressure() {
      return pressure;
    }

    public void setHumidity(int humidity) {
      this.humidity = humidity;
    }
    public int getHumidity() {
      return humidity;
    }

    public void setTempMin(double tempMin) {
      this.tempMin = tempMin;
    }
    public double getTempMin() {
      return tempMin;
    }

    public void setTempMax(double tempMax) {
      this.tempMax = tempMax;
    }
    public double getTempMax() {
      return tempMax;
    }
  }
  public static class Wind {

    @SerializedName("speed")
    double speed;

    @SerializedName("deg")
    int deg;


    public void setSpeed(double speed) {
      this.speed = speed;
    }
    public double getSpeed() {
      return speed;
    }

    public void setDeg(int deg) {
      this.deg = deg;
    }
    public int getDeg() {
      return deg;
    }

  }
  public static class Clouds {

    @SerializedName("all")
    int all;


    public void setAll(int all) {
      this.all = all;
    }
    public int getAll() {
      return all;
    }

  }
  public static class Sys {

    @SerializedName("type")
    int type;

    @SerializedName("id")
    int id;

    @SerializedName("message")
    double message;

    @SerializedName("country")
    String country;

    @SerializedName("sunrise")
    int sunrise;

    @SerializedName("sunset")
    int sunset;


    public void setType(int type) {
      this.type = type;
    }
    public int getType() {
      return type;
    }

    public void setId(int id) {
      this.id = id;
    }
    public int getId() {
      return id;
    }

    public void setMessage(double message) {
      this.message = message;
    }
    public double getMessage() {
      return message;
    }

    public void setCountry(String country) {
      this.country = country;
    }
    public String getCountry() {
      return country;
    }

    public void setSunrise(int sunrise) {
      this.sunrise = sunrise;
    }
    public int getSunrise() {
      return sunrise;
    }

    public void setSunset(int sunset) {
      this.sunset = sunset;
    }
    public int getSunset() {
      return sunset;
    }

  }
}


