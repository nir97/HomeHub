import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.concurrent.TimeUnit;


@WebServlet("/ReviewAdd")

public class ReviewAdd extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    PrintWriter pw = response.getWriter();

    int service_id = Integer.parseInt(request.getParameter("service_id"));
  	String user_id = request.getParameter("user_id");
    String user_name = request.getParameter("user_name");
  	int user_age = Integer.parseInt(request.getParameter("user_age"));
  	String user_gender = request.getParameter("user_gender");
  	double review_rating = Double.parseDouble(request.getParameter("review_rating"));
  	String review_date = request.getParameter("review_date");
  	String review_text = request.getParameter("review_text");

    Review r = new Review(service_id, user_id, user_name, user_age, user_gender, review_rating, review_date, review_text);

    boolean ret = MongoDBDataStoreUtilities.insertReview(r);

    if(ret==true)
		  pw.print("<h4 style='color:red'>Your review is successfully stored.</h4>");
		else
			pw.print("<h4 style='color:red'>MongoDB server is not up and running.</h4>");

    HttpSession session = request.getSession(true);
    if(ret){
      session.setAttribute("review_msg", "Your review is added successfully.");
    }else{
      session.setAttribute("review_msg", "Error adding your review.");
    }
    response.sendRedirect("ShowServiceC?service_id="+service_id);
  }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

    if(utility.isLoggedin() && utility.user_type().equals("customer")) {
		  if(!utility.user_info_set()) {
			response.sendRedirect("MoreInfo");
			return;
		  } else {
					showReviewAddPage(request,response,pw);
		  }
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

  protected void showReviewAddPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);

    pw.print("<div>");

    pw.write("<div class='w3-main' style='margin-top:50px'>");
		pw.write("<div class='w3-container' id='all_content'>");
		pw.write("<hr><h3 style='background-color:#2f2f2f; color:white'><u>Add Review</u></h3>");
    HttpSession session = request.getSession(true);
    if(session.getAttribute("review_msg") != null) {
			pw.print("<h4 style='margin-left:5%;color:green;'>" + session.getAttribute("review_msg") + "</h4>");
			session.removeAttribute("review_msg");
		}
		pw.write("</div>");

    int service_id = Integer.parseInt(request.getParameter("service_id"));

    pw.print("<form action='ReviewAdd' method='post'>");
		pw.print("<input type='hidden' name='service_id' value='"+service_id+"'>");
    pw.print("<input type='hidden' name='user_id' value='"+utility.user_id()+"'>");

		pw.print("<table class='table table-bordered table-hover' id='add-review-table'>");

		pw.print("<tr><td > User Name : </td><td>");
		pw.print("<input type='text' name='user_name' required>");
		pw.print("</td></tr>");

		pw.print("<tr><td > User Age : </td><td>");
		pw.print("<input type='number' name='user_age' min='0' max='200' required>");
		pw.print("</td></tr>");

		pw.print("<tr><td > User Gender: </td><td>");
		pw.print("<select name='user_gender' required>");
		pw.print("<option value='m' selected>Male</option>");
		pw.print("<option value='f'>Female</option>");
		pw.print("<option value='o'>Other</option>");
		pw.print("</select></td></tr>");

		pw.print("<tr><td >Rating: </td>");
		pw.print("<td>");
		pw.print("<select name='review_rating' required>");
		pw.print("<option value='1' selected>1</option>");
		pw.print("<option value='2'>2</option>");
		pw.print("<option value='3'>3</option>");
		pw.print("<option value='4'>4</option>");
		pw.print("<option value='5'>5</option>");
		pw.print("</select></td></tr>");

		pw.print("<tr>");
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Calendar c = Calendar.getInstance();
		String date = sdf.format(c.getTime());
		pw.print("<td><input type='hidden' name='review_date' value='"+date+"'> </td>");
		pw.print("</tr>");

		pw.print("<tr>");
		pw.print("<td >Review Text: </td>");
		pw.print("<td ><textarea name='review_text' rows='4' cols='50' required></textarea></td></tr>");

		pw.print("<tr><td colspan='2'><center><input id='add-review-submit' type='submit' class='btnreview w3-button w3-black' name='ReviewAdd' value='Add Review'></center></td></tr></table>");

		pw.print("</form>");

    pw.print("</div>");

    pw.print("</div>");

  }
}
