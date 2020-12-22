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


@WebServlet("/ReviewView")

public class ReviewView extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

    if(utility.isLoggedin()) {
      if(utility.user_type().equals("customer") || utility.user_type().equals("service_provider")){
        if(!utility.user_info_set()) {
    			response.sendRedirect("MoreInfo");
    			return;
  		  } else {
  					showReviewViewPage(request,response,pw);
  		  }
      }else if(utility.user_type().equals("administrator")){
        showReviewViewPage(request,response,pw);
      }
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

  protected void showReviewViewPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);

    pw.print("<div>");

    pw.write("<div class='w3-main'>");
		pw.write("<div class='w3-container' id='all_content'>");
		pw.write("<hr><h3 style='background-color:#2f2f2f; color:white'><u>Reviews of this service</u></h3>");
		pw.write("</div>");

    int service_id = Integer.parseInt(request.getParameter("service_id"));

    HashMap<Integer, ArrayList<Review>> hm = MongoDBDataStoreUtilities.getReviews();

    if(hm!=null){
      if(hm.containsKey(service_id)){
            int counter = 0;
						for(Review r : hm.get(service_id)){
							pw.print("<table class='gridtable' style='border: 2px solid #000000;border-radius: 12px;padding: 10px; width:100%'>");
							counter++;
							pw.print("<tr>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>"+counter+"). </td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>User Name: </td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>" +r.user_name+ "</td>");
							pw.print("</tr>");
              pw.println("<tr>");
              pw.print("<td></td>");
              pw.println("<td style='padding: 10px;border-bottom: 2px solid #000000;'>User Age: </td>");
              pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>" +r.user_age+ "</td>");
              pw.print("</tr>");
              pw.println("<tr>");
              pw.print("<td></td>");
              pw.println("<td style='padding: 10px;border-bottom: 2px solid #000000;'>User Gender: </td>");
              pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>" +r.user_gender+ "</td>");
              pw.print("</tr>");
              pw.println("<tr>");
							pw.print("<td></td>");
							pw.println("<td style='padding: 10px;border-bottom: 2px solid #000000;'>Review Rating: </td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>" +r.review_rating+ "</td>");
							pw.print("</tr>");
							pw.print("<tr>");
							pw.print("<td></td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>Review Date: </td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>" +r.review_date+ "</td>");
							pw.print("</tr>");
							pw.print("<tr>");
							pw.print("<td></td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>Review Text: </td>");
							pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>" +r.review_text+ "</td>");
							pw.print("</tr>");

							pw.println("</table>");
							pw.println("<br>");
						}
      }else{
        pw.println("<h2>There are no reviews for this service.</h2>");
      }
    }else{
      pw.print("<h4 style='color:red'>MongoDB server is not up and running.</h4>");
    }

    pw.print("</div>");

    pw.print("</div>");

  }
}
