package zad1;

import javafx.application.Platform;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class GUI {
    private static final String WIKI_URL = "https://pl.wikipedia.org/wiki/";
    private static JLabel weatherValueLabel = new JLabel();
    private static JLabel currencyValueLabel = new JLabel();
    private static JLabel plnRateValueLabel = new JLabel();
    private WebView browser;

    public GUI() {
        JFrame frame = new JFrame("TPO 2");

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        JPanel weatherPanel = createInfoPanel("Weather");
        JPanel currencyPanel = createInfoPanel("Currency rate");
        JPanel plnRatePanel = createInfoPanel("NBP PLN Rate");
        JButton changeDataButton = new JButton("Change Data");
        topPanel.add(weatherPanel);
        topPanel.add(currencyPanel);
        topPanel.add(plnRatePanel);
        topPanel.add(changeDataButton);


        JPanel wikiPanel = new JPanel(new BorderLayout());
        JFXPanel jfxPanel = new JFXPanel();

        wikiPanel.add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> browser = new WebView());


        frame.add(topPanel, BorderLayout.WEST);
        frame.add(wikiPanel, BorderLayout.EAST);

        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        weatherPanel.add(weatherValueLabel);
        currencyPanel.add(currencyValueLabel);
        plnRatePanel.add(plnRateValueLabel);

        Dimension panelSize = new Dimension(200, 100);
        weatherPanel.setPreferredSize(panelSize);
        currencyPanel.setPreferredSize(panelSize);
        plnRatePanel.setPreferredSize(panelSize);


        changeDataButton.addActionListener(e -> {
            JTextField countryField = new JTextField(10);
            JTextField cityField = new JTextField(10);
            JTextField currencyField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Country:"));
            panel.add(countryField);
            panel.add(new JLabel("City:"));
            panel.add(cityField);
            panel.add(new JLabel("Currency:"));
            panel.add(currencyField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Enter args (in English)",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String country = countryField.getText();
                String city = cityField.getText();
                String currency = currencyField.getText().trim();


                Service service = new Service(country);
                try {
                    String weatherData = service.getWeather(city);
                    JSONObject jsonObject = new JSONObject(weatherData);

                    Double temp = jsonObject.getJSONObject("main").getDouble("temp");
                    Double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
                    Double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
                    String text = String.format(
                            "<html> loc : %s <br>" + "temp : %f <br>" + "pressure : %f <br>" + "humidity : %f <br> <html>",
                            city, temp, pressure, humidity
                    );
                    weatherValueLabel.setText(text);
                } catch (FetchDataException ex) {
                    weatherValueLabel.setText(ex.getMessage());
                } catch (Exception ex) {
                    weatherValueLabel.setText("Something went wrong");
                }

                try {
                    Double currencyRate = service.getRateFor(currency);
                    currencyValueLabel.setText(currencyRate.toString());
                } catch (Exception ex) {
                    currencyValueLabel.setText(ex.getMessage());
                }

                try {
                    Double plnRate = service.getNBPRate();
                    plnRateValueLabel.setText(plnRate.toString());
                } catch (Exception ex) {
                    plnRateValueLabel.setText(ex.getMessage());
                }
                loadPage(jfxPanel, WIKI_URL + city);
            }
        });
    }

    private void loadPage(JFXPanel jfxPanel, String url) {
        Platform.runLater(() -> {
            WebEngine webEngine = browser.getEngine();
            webEngine.load(url);
            Scene scene = new Scene(browser);
            jfxPanel.setScene(scene);
        });
    }

    private static JPanel createInfoPanel(String header) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel(header);
        panel.add(headerLabel);

        return panel;
    }
}
