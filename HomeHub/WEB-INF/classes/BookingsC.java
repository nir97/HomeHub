import java.io.*;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/BookingsC")

public class BookingsC extends HttpServlet {

  private String msg = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		if(utility.isLoggedin() && utility.user_type().equals("customer")) {
			if(!utility.user_info_set()) {
				response.sendRedirect("MoreInfo");
				return;
			} else {
				displayBookings(request, response, pw);
			}
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

	protected void displayBookings(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

			Utilities utility = new Utilities(request, pw);
			HttpSession session = request.getSession(true);
			String userId = String.valueOf(session.getAttribute("user_id"));
			HashMap<Integer,Transaction> hm = MySqlDataStoreUtilities.getTransactions(userId);

			utility.printHtml("Head.html");
			utility.printCHeader();
			utility.printCLeftNav();

			pw.print("<div class='container-fluid text-center' id='cust-bookings-body'>");
			pw.print("<div class='row content'>");
			pw.print("<h3 id='sp-labels'> <u>Bookings Overview</u></h3>");

			// ==== Pending bookings
			pw.print("<div  class='col-sm-12' id='cust-pend-headers'>");
			pw.print("<h3 id='sp-header-text'> Pending Bookings</h3>");
			pw.print("</div>");
			pw.print("<div  class='col-sm-12' id='cust-booking-tables-div'>");
			pw.print("<table class='table table-bordered table-hover' id='cust-booking-tables'>");
			pw.print("<tr><th>Id</th><th>Provider Name</th><th>Service Name</th><th>Category</th><th>Sub-category</th><th>Booking date</th><th>transaction status</th></tr>");

			int count_p=0;
			for(Map.Entry<Integer,Transaction> entry : hm.entrySet()){
			  Transaction transaction = entry.getValue();
			  if(transaction.service_status.equalsIgnoreCase("pending") && transaction.is_cancelled == false){
				pw.print("<tr><td><a href='BookingDetailsC?transaction_id="+transaction.transaction_id+"'>"+transaction.transaction_id+"</a></td><td>"+transaction.service_provider_name+"</td><td>"+transaction.service_name+"</td><td>"+transaction.service_category_super+"</td><td>"+transaction.service_category_sub+"</td><td>"+transaction.booking_date+"</td><td>"+transaction.transaction_status+"</td></tr>");
						count_p+=1;
			  }
			}

			if(count_p==0){
					pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No pending bookings found </h3></div>");
			}
			pw.print("</table>");
			pw.print("</div>");

			// // ==== Upcoming bookings
			// pw.print("<div  class='col-sm-12' id='cust-upcmg-headers'>");
			// pw.print("<h3 id='sp-header-text'>Upcoming Bookings</h3>");
			// pw.print("</div>");
			// pw.print("<div  class='col-sm-12' id='cust-upcmg-tables'>");
			// pw.print("<table class='table table-bordered table-hover' id='cust-upcmg-tables'>");
			// pw.print("<tr><th>Id</th><th>Provider Name</th><th>Category</th><th>Sub-category</th><th>Booking date</th><th>Service date</th></tr>");
			// int count_a=0;
			// for(Map.Entry<Integer,Transaction> entry : hm.entrySet()){
			//   Transaction transaction = entry.getValue();
			//   if(transaction.service_status.equalsIgnoreCase("upcoming")){
			// 	pw.print("<tr><td><a href='ShowServiceA?service_id="+transaction.transaction_id+"'>"+transaction.transaction_id+"</a></td><td>"+transaction.service_provider_name+"</td><td>"+transaction.service_category_super+"</td><td>"+transaction.service_category_sub+"</td><td>"+transaction.booking_date+"</td><td>"+transaction.service_date+"</td></tr>");
			// 			count_a+=1;
			//   }
			// }
      //
			// if(count_a==0){
			// 	pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No accepted services found </h3></div>");
			// }
			// pw.print("</table>");
			// pw.print("</div>");

			// ==== Completed bookings
			pw.print("<div  class='col-sm-12' id='cust-compl-headers'>");
			pw.print("<h3 id='sp-header-text'>Booking history</h3>");
			pw.print("</div>");
			pw.print("<div  class='col-sm-12' id='cust-compl-tables-div'>");
			pw.print("<table class='table table-bordered table-hover' id='cust-compl-tables'>");
			pw.print("<tr><th>Id</th><th>Provider Name</th><th>Service Name</th><th>Category</th><th>Sub-category</th><th>Booking date</th><th>Service date</th><th>Actual Service Date</th><th>transaction status</th><th>service status</th><th>is cancelled</th><th>is delivered on time</th></tr>");
			int count_r=0;
			for(Map.Entry<Integer,Transaction> entry : hm.entrySet()){
			  Transaction transaction = entry.getValue();
			  if(!(transaction.service_status.equalsIgnoreCase("pending") && transaction.is_cancelled == false)){
				pw.print("<tr><td><a href='BookingDetailsC?transaction_id="+transaction.transaction_id+"'>"+transaction.transaction_id+"</a></td><td>"+transaction.service_provider_name+"</td><td>"+transaction.service_name+"</td><td>"+transaction.service_category_super+"</td><td>"+transaction.service_category_sub+"</td><td>"+transaction.booking_date+"</td><td>"+transaction.service_date+"</td><td>"+transaction.actual_service_date+"</td><td>"+transaction.transaction_status+"</td><td>"+transaction.service_status+"</td><td>"+transaction.is_cancelled+"</td><td>"+transaction.is_delivered_on_time+"</td></tr>");
						count_r+=1;
			  }
			}

			if(count_r==0){
					pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No history found </h3></div>");
			}
			pw.print("</table>");
			pw.print("</div>");
			pw.print("</div>");
			pw.print("</div>");

			utility.printHtml("Footer.html");

	}

}
