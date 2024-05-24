package tpo.zad5_servlets;

import tpo.zad5_servlets.repo.CarRepo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SelectCar", value = "/select-car")
public class SelectCarServlet extends HttpServlet {
    private CarRepo carRepo = new CarRepo();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Wybierz Rodzaj Auta</title></head>");
        out.println("<body style=\"background-color: darkkhaki; color: #72ff9a\" >");
        out.println("<h1 style=\"text-align: center; font-size: 50px\">Wybierz rodzaj auta</h1>");
        out.println("<form action='/cars' method='POST'>");
        out.println("<select name='carType' >");

        for (String type : carRepo.getTypes()) {
            out.println("<option value='" + type + "'>" + type + "</option>");
        }

        out.println("</select>");
        out.println("<input type='submit' value='Wybierz'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }


}