import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.*;

@WebServlet("/CheckOut")

public class CheckOut extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

    if(utility.isLoggedin() && utility.user_type().equals("customer")) {
      if(!utility.user_info_set()) {
        response.sendRedirect("MoreInfo");
        return;
      } else {
				storeOrders(request, response, pw);
      }
		} else {
			response.sendRedirect("Home");
			return;
		}
	}

	protected void storeOrders(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {
	    try{
  			Utilities utility = new Utilities(request,pw);
  			HttpSession session=request.getSession();

  			String user_id = utility.user_id();

			utility.printHtml("Head.html");
			utility.printCHeader();
			utility.printCLeftNav();

			HashMap<Integer, Service> hm_s = MySqlDataStoreUtilities.getServices();
    		HashMap<String, User> hm_u = MySqlDataStoreUtilities.getUsers();

			if(hm_s!=null && hm_u!=null){
				User u = hm_u.get(user_id);

				pw.print("<div class='container-fluid text-center'>");
    			pw.print("<div class='row content'>");
    			pw.print("<div class='col-sm-8 text-left w3-card w3-round w3-white cust-search-res' id='checkout-div'>");
    			pw.print("<div class='w3-container '>");

				pw.print("<form action='Payment' method='post'>");
    			pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
    			pw.print("<a style='font-size: 24px;'>Order Details</a>");
    			pw.print("</h2><div class='entry'>");

    			pw.print("<table id='checkout-table' class='table table-bordered table-hover'><tr><th>User ID : </th><td colspan='4'> <center>"+user_id+" </center></td></tr>");
    			pw.print("<tr><th>Index</th><th>Service Name</th><th>Rate</th><th>Hours</th><th>Total Amount</th></tr>");
				int ind = 1;
  				double total_price = 0;
  				for (Map.Entry<Integer, Integer> entry: utility.getCustomerOrders().entrySet()){
  					int service_id = entry.getKey();
  					int hours = entry.getValue();
  					Service s = hm_s.get(service_id);

  					pw.print("<tr>");
  					pw.print("<td>"+ind+".)</td><td>"+s.service_name+"</td><td>"+s.service_rate+"</td><td> "+hours+"</td><td>"+(s.service_rate * hours)+"</td>");
  					pw.print("</tr>");

  					total_price += (hours * s.service_rate);
  					ind++;
  				}

  				pw.print("<tr><th>Final Amount : </th><th colspan='4' style='text-align:right'>$"+total_price);
  				pw.print("</td><tr>");
  				pw.print("</table>");

          pw.print("<table id='checkout-table'><tr></tr><tr></tr>");

          pw.print("<tr><th>");
          pw.print("Customer Name</th>");
          pw.print("<td><input type='text' name='customer_name' required>");
          pw.print("</td></tr>");

					pw.print("<tr><th>Customer Gender</th><td>");
					pw.print("<select name='customer_gender' required>");
					pw.print("<option value='m' selected>Male</option>");
					pw.print("<option value='f'>Female</option>");
					pw.print("<option value='o'>Other</option>");
					pw.print("</select></td></tr>");

          pw.print("<tr><th>");
          pw.print("Customer age</th>");
          pw.print("<td><input type='number' name='customer_age' min='0' max='200' required>");
          pw.print("</td></tr>");

					pw.print("<tr><th>");
          pw.print("Customer Phone</th>");
          pw.print("<td><input type='text' name='customer_phone' required>");
          pw.print("</td></tr>");

          pw.print("<tr><th>");
          pw.print("Credit/account Number</th>");
          pw.print("<td><input type='number' name='creditcard_account_no' min='0' required>");
          pw.print("</td></tr>");

          String address = u.user_street1 + ", " + u.user_street2 + ", " + u.user_city + ", " + u.user_state + " " + u.user_zip + ", " + u.user_country;
          pw.print("<tr><th>Customer Address :</th><td>"+address+"</td></tr>");
          pw.print("<tr><td colspan='2'><center><br><input style='width:250px' type='submit' name='payment' value='Payment' class='btnbuy w3-button w3-black'></center></td></tr>");
          pw.print("</table>");
          pw.print("</div>");
          pw.print("</div>");
          pw.print("</form>");

          pw.print("</div>");
    	  pw.print("</div>");
          pw.print("</div>");
          pw.print("</div>");
        }else{
          pw.print("<h4 style='margin-left:5%;color:red;'>MySQL server is not up and running.</h4>");
        }

        utility.printHtml("Footer.html");
	    }catch(Exception e){
        System.out.println(e.getMessage());
	    }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
	}
}
