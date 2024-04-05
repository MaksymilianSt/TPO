/**
 *
 *  @author Stachnik Maksymilian S25304
 *
 */

package zad1;


import javax.swing.*;

public class Main {
  public static void main(String[] args){
    Service s = new Service("Canada");
    String weatherJson = s.getWeather("Toronto");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater(GUI::new);
  }
}
