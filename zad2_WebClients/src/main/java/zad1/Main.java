/**
 *
 *  @author Stachnik Maksymilian S25304
 *
 */

package zad1;


import java.util.Locale;

public class Main {
  public static void main(String[] args) throws Exception{
    Service s = new Service("Canada");
    String weatherJson = s.getWeather("Toronto");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    System.out.println("weatherJson = " + weatherJson);
    System.out.println("rate1 = " + rate1);
  }
}
