package tpo.zad5_servlets;

import tpo.zad5_servlets.model.Car;
import tpo.zad5_servlets.repo.CarRepo;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "listCars", value = "/cars")
public class SelectedByTypeServlet extends HttpServlet {
    private CarRepo carRepo = new CarRepo();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String selectedCarType = request.getParameter("carType");

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Wybrany Rodzaj Auta</title></head>");
        out.println("<body style=\"background-color: darkkhaki; color: #7731e3\" >");
        out.println("<h1 style=\"text-align: center; font-size: 50px\">Wybrałeś  auta typu : " + selectedCarType + "</h1>");
        out.println("<hr> <br> <br> <br>");
        out.println("<div style=\"text-align: center;\">");
        out.println("<table style=\"text-align: center; font-size: 50px\" >");
        out.println("<tr style=\"color: coral;\">");
        out.println("<th style=\"padding: 30px\">rodzaj</th>");
        out.println("<th style=\"padding: 30px\">marka</th>");
        out.println("<th style=\"padding: 30px\">rokProdukcji</th>");
        out.println("<th style=\"padding: 30px\">zuzyciePaliwa</th>");
        out.println("</tr>");
        out.println("<hr>");
        for (Car car : carRepo.getCarsByType(selectedCarType)) {
            out.println(car.getHtmlRow());
        }
        out.println("</table>");
        out.println("<div>");
        out.println("</body>");
        out.println("</html>");
    }

    public void destroy() {
    }
}