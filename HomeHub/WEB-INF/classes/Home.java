import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/Home")


public class Home extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

		if(!utility.isLoggedin()){
			utility.printHtml("Head.html");
			utility.printHtml("Home.html");
			utility.printHtml("Footer.html");
		} else {
			if(utility.user_type().equals("customer")) {
				response.sendRedirect("DashboardCustomer");
				return;
			} else if(utility.user_type().equals("service_provider")) {
				response.sendRedirect("DashboardServiceProvider");
				return;
			} else if(utility.user_type().equals("administrator")) {
				response.sendRedirect("DashboardAdministrator");
				return;
			}
		}

	}

}
