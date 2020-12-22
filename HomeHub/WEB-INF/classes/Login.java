import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet {

	private String error_msg = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		String user_id = request.getParameter("user_id");
		String user_pswd = request.getParameter("user_pswd");
		String user_type = request.getParameter("user_type");

		HashMap<String, User> hm=MySqlDataStoreUtilities.getUsers();

		if(hm == null) {
			this.error_msg = "MySQL server is not up and running.";
		} else {
			User user = hm.get(user_id);
			if(user != null) {
				String id = user.user_id;
				String pswd = user.user_pswd;
				String type = user.user_type;
				Boolean info_set = user.user_info_set;
			  if(pswd.equals(user_pswd) && type.equals(user_type)) {
				  utility.login(user.user_id, user.user_type, user.user_info_set);
				  response.sendRedirect("Home");
				  return;
				} else {
					this.error_msg = "Please check your Password and User Type!";
				}
			} else {
				this.error_msg = "Please check your UserID!";
			}
		}
		displayLoginPage(request, response, pw);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		if(utility.isLoggedin()) {
			response.sendRedirect("Home");
			return;
		}
		displayLoginPage(request, response, pw);
	}

	protected void displayLoginPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

		Utilities utility = new Utilities(request, pw);
		HttpSession session = request.getSession(true);
		utility.printHtml("Head.html");
		utility.printLoginHeader();

		pw.print("<body>");
		pw.print("<div class='container-fluid'>");
		pw.print("<div class='row content'>");
		pw.print("<div class='col-sm-3 sidenav' id='login-sidenav'>");
		pw.print("<div class='span4'><img class='center-block' src='images/login/logo.png' /></div>");
		pw.print("</div>");

		pw.print("<div class='col-sm-9'>");
		pw.print("<br><br><br>");
			pw.print("<h2 id='sign-in'>Welcome, Do we know you ?</h2>");

		if(this.error_msg != null) {
			pw.print("<h4 style='margin-left:5%;color:red;'>" + this.error_msg + "</h4>");
			this.error_msg = null;
		}
		if(session.getAttribute("login_msg") != null) {
			pw.print("<h4 style='margin-left:5%;color:green;'>" + session.getAttribute("login_msg") + "</h4>");
			session.removeAttribute("login_msg");
		}

		pw.print("<form class='form-horizontal' method='post' action='Login'>");
		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<input type='text' class='form-control' id='user_id' placeholder='UserID' name='user_id' required>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<input type='password' class='form-control' id='user_pswd' placeholder='Password' name='user_pswd' required>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<select class='form-control' id='user_type' name='user_type'>");
		pw.print("<option id='user_type_customer' value='customer' selected>Customer</option>");
		pw.print("<option id='user_type_service_provider' value='service_provider'>Service Provider</option>");
		pw.print("<option id='user_type_administrator' value='administrator'>Administrator</option>");
		pw.print("</select>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-offset-2 col-sm-10'>");
		pw.print("<button type='submit' class='btn btn-default'>Submit</button>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("</form>");

		pw.print("<br>");
		pw.print("<h4 id='register-label'>Don't have an account? <a href='Registration'>Register</a></h4>");
		pw.print("</div>");

		pw.print("</div>");
		pw.print("</div>");

		utility.printHtml("Footer.html");
	}

}
