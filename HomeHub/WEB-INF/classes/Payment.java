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
import java.text.SimpleDateFormat;

@WebServlet("/Payment")

public class Payment extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		if(utility.isLoggedin() && utility.user_type().equals("customer")) {
			if(!utility.user_info_set()) {
				response.sendRedirect("MoreInfo");
				return;
			} else {
				storePayments(request, response, pw);
			}
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

	protected void storePayments(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {
	  try{
  		Utilities utility = new Utilities(request,pw);
  		HttpSession session=request.getSession();

			Random ran = new Random();

  		String user_id = utility.user_id();

			utility.printHtml("Head.html");
			utility.printCHeader();
			utility.printCLeftNav();

			HashMap<Integer, Service> hm_s = MySqlDataStoreUtilities.getServices();
    	HashMap<String, User> hm_u = MySqlDataStoreUtilities.getUsers();

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String booking_date = formatter.format(new java.util.Date());

			if(hm_s!=null && hm_u!=null){
				User u = hm_u.get(user_id);

				pw.print("<div class='container-fluid text-center'>");
    		pw.print("<div class='row content'>");
    		pw.print("<div class='col-sm-8 text-left w3-card w3-round w3-white cust-search-res' id='checkout-div'>");
    		pw.print("<div class='w3-container '>");
				int ind = 1;

				int transactionId = MySqlDataStoreUtilities.transactionTableSize();
				ArrayList<Integer> st = new ArrayList();
  			for(Map.Entry<Integer, Integer> entry: utility.getCustomerOrders().entrySet()){
  				int service_id = entry.getKey();
					int hours = entry.getValue();
					Service s = hm_s.get(service_id);
					User sp = hm_u.get(s.service_provider_id);

					Transaction t = new Transaction((++transactionId),
													u.user_id,
													u.user_email,
													request.getParameter("customer_name"),
													request.getParameter("customer_gender"),
													Integer.parseInt(request.getParameter("customer_age")),
													request.getParameter("customer_phone"),
													u.user_street1,
													u.user_street2,
													u.user_city,
													u.user_state,
													u.user_zip,
													u.user_country,
													u.user_lat,
													u.user_long,
													Integer.parseInt(request.getParameter("creditcard_account_no")),
													s.service_id,
													s.service_name,
													s.service_category_super,
													s.service_category_sub,
													s.service_rate,
													s.service_provider_id,
													sp.user_email,
													sp.user_name,
													sp.user_gender,
													sp.user_age,
													sp.user_phone,
													sp.user_street1,
													sp.user_street2,
													sp.user_city,
													sp.user_state,
													sp.user_zip,
													sp.user_country,
													sp.user_lat,
													sp.user_long,
													hours,
													(hours * s.service_rate),
													booking_date,
													"",
													"",
													(ran.nextBoolean()) ? "approved" : "disputed",
													"pending",
													false,
													false);

					boolean transactionInsertStatus = MySqlDataStoreUtilities.insertPayment(t);

					if( transactionInsertStatus ){
						pw.print("<h1 style='color:green'> Updated transaction for service "+s.service_name+". Checking bookings for further information </h1>");
						st.add(service_id);
					}else{
						pw.print("<h1 style='color:red'> Error processing transaction for "+s.service_name+".</h1>");
					}
  			}
				for(Integer sid : st){
					utility.removeCart(sid);
				}
				pw.print("</div>");
				pw.print("</div>");
				pw.print("</div>");
				pw.print("</div>");
        }else{
          pw.print("<h4 style='margin-left:5%;color:red;'>MySQL server is not up and running.</h4>");
        }

        utility.printHtml("Footer.html");
	    }catch(Exception e){
			e.printStackTrace();
	    }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
	}
}
