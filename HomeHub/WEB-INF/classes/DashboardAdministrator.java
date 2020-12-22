import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/DashboardAdministrator")


public class DashboardAdministrator extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

		if(utility.isLoggedin() && utility.user_type().equals("administrator")) {
			response.sendRedirect("ShowServicesA");
		} else {
			response.sendRedirect("Login");
			return;
		}

	}

}
