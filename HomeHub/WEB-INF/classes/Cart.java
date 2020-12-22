import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/Cart")

public class Cart extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		if(utility.isLoggedin() && utility.user_type().equals("customer")) {
			if(!utility.user_info_set()) {
				response.sendRedirect("MoreInfo");
				return;
			} else {
				displayCart(request, response, pw);
			}
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

  protected void displayCart(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {

		response.setContentType("text/html");
		HttpSession session = request.getSession(true);
		Utilities utility = new Utilities(request,pw);

		utility.printHtml("Head.html");
		utility.printCHeader();
		utility.printCLeftNav();

		HashMap<Integer, Service> hm_s = MySqlDataStoreUtilities.getServices();
		HashMap<String, User> hm_u = MySqlDataStoreUtilities.getUsers();

    if(hm_s != null && hm_u!=null){

      pw.print("<div class='container-fluid text-center'>");
			pw.print("<div class='row content'>");
			pw.print("<div class='col-sm-8 text-left w3-card w3-round w3-white cust-search-res' id='user-profile-sec'>");
			pw.print("<div class='w3-container '>");

      if(session.getAttribute("cart_msg")!=null){
  			pw.print("<h4 style='color:red'>"+session.getAttribute("cart_msg")+"</h4>");
  			session.removeAttribute("cart_msg");
  		}

      if(utility.CartCount() > 0){
        pw.print("<table class='table table-bordered table-hover'>");
				pw.print("<tr><th>Index</th><th>Service Name</th><th>Hourly Rate</th><th>Hours</th><th>Total Amount</th><th>Action</th></tr>");

				int ind = 1;
				double total_price = 0;

				for (Map.Entry<Integer, Integer> entry : utility.getCustomerOrders().entrySet()) {
					int service_id = entry.getKey();
					int hours = entry.getValue();

					Service s = hm_s.get(service_id);

					pw.print("<tr>");
					pw.print("<td>"+ind+".)</td><td>"+s.service_name+"</td><td>"+s.service_rate+"</td><td>"+hours+"</td><td>"+(hours * s.service_rate)+"</td><td><a href='ShowServiceC?service_id="+s.service_id+"'>update</a></td>");
					pw.print("</tr>");

					total_price += (hours * s.service_rate);
					ind++;
				}

				pw.print("<tr><th colspan='4'> <center>Final Amount</center></th><th colspan='2'>: $"+total_price+"</th></tr>");
				pw.print("<form name ='Cart' action='CheckOut' method='post'>");
				pw.print("<tr><td colspan='6'> <center><input id='cart-checkout-btn' type='submit' name='CheckOut' value='CheckOut' class='btnbuy w3-button w3-black'> </center></td></tr>");
				pw.print("</form></table>");

      }else{
        pw.print("<h4 style='color:red'>Your Cart is empty</h4>");
      }

			Carousel c = new Carousel();
			pw.print(c.carouselfeature(utility));

      pw.print("</div>");
			pw.print("</div>");
      pw.print("</div>");
      pw.print("</div>");

    }else{
      pw.print("<h4 style='margin-left:5%;color:red;'>MySQL server is not up and running.</h4>");
    }

    utility.printHtml("Footer.html");
	}

}
