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


@WebServlet("/CartUpdate")

public class CartUpdate extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    String sid = request.getParameter("service_id");
    int service_id = Integer.parseInt(sid);
    String action = request.getParameter("action");

    if(action.equalsIgnoreCase("add to cart")){
      int hours = Integer.parseInt(request.getParameter("hours"));
      int new_count = hours;
      utility.updateCart(service_id, new_count);
    }
    if(action.equalsIgnoreCase("update cart")){
      int hours = Integer.parseInt(request.getParameter("hours"));
      int new_count = hours;
      utility.updateCart(service_id, new_count);
    }
    if(action.equalsIgnoreCase("remove from cart")){
      utility.removeCart(service_id);
    }

    HttpSession session = request.getSession(true);
    session.setAttribute("cart_msg", "Your cart is updated.");
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
					showCartUpdatePage(request,response,pw);
		  }
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

  protected void showCartUpdatePage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);

    int service_id = Integer.parseInt(request.getParameter("service_id"));

    pw.print("<div class='col-sm-2 w3-card w3-round w3-white' id='cust-sp-detail-info-action-sec'>");
    pw.print("<div class='w3-container' >");
    pw.print("<h4 class='w3-center'>Action</h4>");

    pw.print("<form action='CartUpdate' method='post'>");
    pw.print("<input type='hidden' name='service_id' value='"+service_id+"'>");
    pw.print("<label>Hours of work : </label><input type='number' min='1' max='8' name='hours' value='"+utility.cartItemCount(service_id)+"' required>");
    if(utility.cartItemCount(service_id) == 0){
      pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' name='action' value='Add to Cart'>");
    }else{
      pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' name='action' value='Update Cart'>");
      pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' name='action' value='Remove from Cart'>");
    }
    pw.print("</form>");
    pw.print("</div>");
    pw.print("</div>");

  }
}
