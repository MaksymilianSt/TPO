package tpo.zad5_servlets.model;

import java.io.Serializable;

public class Car implements Serializable {
    public String rodzaj;
    public String marka;
    public String rokProdukcji;
    public double zuzyciePaliwa;

    public Car(String rodzaj, String marka, String rokProdukcji, double zuzyciePaliwa) {
        this.rodzaj = rodzaj;
        this.marka = marka;
        this.rokProdukcji = rokProdukcji;
        this.zuzyciePaliwa = zuzyciePaliwa;
    }

    @Override
    public String toString() {
        return "Car{" +
                "rodzaj='" + rodzaj + '\'' +
                ", marka='" + marka + '\'' +
                ", rokProdukcji='" + rokProdukcji + '\'' +
                ", zuzyciePaliwa=" + zuzyciePaliwa +
                '}';
    }
    public String getHtmlRow() {
        return "<tr>" + "<th style=\"color: #72ff9a\">" + rodzaj + "</th>"+ "<th style=\"color: #72ff9a\">" + marka + "</th>" + "\"<th style=\"color: #72ff9a\">\"" + rokProdukcji + "</th>" +
                "<th style=\"color: #72ff9a\">" + zuzyciePaliwa + "</th>" + "</tr>";
    }
}
