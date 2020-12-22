import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Logout")

public class Logout extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);
    utility.logout();
    response.sendRedirect("Home");
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);
    utility.logout();
    response.sendRedirect("Home");
		return;
	}

}
