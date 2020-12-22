import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Registration")


public class Registration extends HttpServlet {

	private String error_msg;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		String user_id = request.getParameter("user_id");
		String user_pswd = request.getParameter("user_pswd");
		String user_re_pswd = request.getParameter("user_re_pswd");
		String user_type = request.getParameter("user_type");

		if(!user_pswd.equals(user_re_pswd)) {
			this.error_msg = "Passwords doesn't match!";
		} else {
      HashMap<String, User> hm = MySqlDataStoreUtilities.getUsers();
			if(hm == null) {
				this.error_msg = "MySQL server is not up and running.";
			} else {
				if(hm.containsKey(user_id)) {
					this.error_msg = "UserID already exist.";
				} else {
					User user = new User(user_id,user_pswd,user_type,false,"","","","",-1,"","","","","","","","",0.0,0.0);
					boolean ret = MySqlDataStoreUtilities.insertUser(user);
          if(ret == false) {
            this.error_msg = "MySQL server is not up and running.";
          } else {
            HttpSession session = request.getSession(true);
  					session.setAttribute("login_msg", "Your " + user_type + " account has been created. Please login.");
  					response.sendRedirect("Login");
            return;
          }
				}
			}
		}
		displayRegistrationPage(request, response, pw);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);
    if(utility.isLoggedin()) {
      response.sendRedirect("Login");
      return;
    }
		displayRegistrationPage(request, response, pw);
	}

	protected void displayRegistrationPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);

    utility.printHtml("Head.html");
    utility.printRegistrationHeader();

	pw.print("<body>");
	pw.print("<div class='container-fluid'>");
	pw.print("<div class='row content'>");
	pw.print("<div class='col-sm-3 sidenav' id='login-sidenav'>");
	pw.print("<div class='span4'><img class='center-block' src='images/login/logo.png' /></div>");
	pw.print("</div>");

    pw.print("<div class='col-sm-9'>");
    pw.print("<br><br><br>");
	pw.print("<h2 id='sign-in'>Register to HomeHub.</h2>");

    if(this.error_msg != null) {
			pw.print("<h4 style='margin-left:5%;color:red;'>" + this.error_msg + "</h4>");
      this.error_msg = null;
    }

    pw.print("<form class='form-horizontal'  method='post' action='Registration'>");
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
    pw.print("<input type='password' class='form-control' id='user_re_pswd' placeholder='Retype Password' name='user_re_pswd' required>");
    pw.print("</div>");
    pw.print("</div>");
    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<label class='form-check-label' style='margin-right: 10px'> You are a </label>");
    pw.print("<input class='form-check-input' type='radio' name='user_type' id='user_type_customer' value='customer' checked> Customer");
    pw.print("<input class='form-check-input' type='radio' name='user_type' id='user_type_service_provider' value='service_provider'> Service Provider");
    pw.print("<input class='form-check-input' type='radio' name='user_type' id='user_type_administrator' value='administrator'> Administrator");
    pw.print("</div>");
    pw.print("</div>");
    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-offset-2 col-sm-10'>");
    pw.print("<button type='submit' class='btn btn-default'>Submit</button>");
    pw.print("</div>");
    pw.print("</div>");
    pw.print("</div>");
    pw.print("</form>");
    pw.print("</div>");

    pw.print("</div>");
    pw.print("</div>");

    utility.printHtml("Footer.html");

	}

}
