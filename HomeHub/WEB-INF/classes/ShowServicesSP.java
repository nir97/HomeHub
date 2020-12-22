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

@WebServlet("/ShowServicesSP")


public class ShowServicesSP extends HttpServlet {

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

		if(utility.isLoggedin() && utility.user_type().equals("service_provider")) {
      if(!utility.user_info_set()) {
        response.sendRedirect("MoreInfo");
        return;
      } else {
				displayShowServicesPage(request, response, pw);
      }
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

	protected void displayShowServicesPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);
    HttpSession session = request.getSession(true);

    HashMap<Integer,Service> hm = MySqlDataStoreUtilities.getServices();

    utility.printHtml("Head.html");
    utility.printSPHeader();
    utility.printSPLeftNav();

    pw.print("<div class='container-fluid text-center' id='spStatusBody'>");
    pw.print("<div class='row content'>");
    pw.print("<h3 id='sp-labels'> <u>Services Overview</u></h3>");

    pw.print("<div  class='col-sm-12' id='sp-act-headers'>");
    pw.print("<h3 id='sp-header-text'>Active Services</h3>");
    pw.print("</div>");
    pw.print("<div  class='col-sm-12' id='sp-tables'>");
    pw.print("<table class='table table-bordered table-hover' id='sp-tables'>");
    pw.print("<tr><th>Id</th><th>Name</th><th>Category</th><th>Sub-category</th><th>Rate</th><th>Admin-Status</th></tr>");
		int count_a=0;
		for(Map.Entry<Integer,Service> entry : hm.entrySet()){
      Service service = entry.getValue();
      if(service.service_provider_id.equals(utility.user_id()) && service.service_status.equalsIgnoreCase("active")){
        pw.print("<tr><td><a href='ShowServiceSP?service_id="+service.service_id+"' >"+service.service_id+"</a></td><td>"+service.service_name+"</td><td>"+service.service_category_super+"</td><td>"+service.service_category_sub+"</td><td>"+service.service_rate+"</td><td>"+service.service_admin_status+"</td></tr>");
				count_a+=1;
      }
    }
		if(count_a==0){
			pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No active services found </h3></div>");
		}
    pw.print("</table>");
    pw.print("</div>");

    pw.print("<div  class='col-sm-12' id='sp-inactv-headers'>");
    pw.print("<h3 id='sp-header-text'>Inactive Services</h3>");
    pw.print("</div>");
    pw.print("<div  class='col-sm-12' id='sp-tables'>");
    pw.print("<table class='table table-bordered table-hover' id='sp-tables'>");
    pw.print("<tr><th>Id</th><th>Name</th><th>Category</th><th>Sub-category</th><th>Rate</th><th>Admin-Status</th></tr>");
		int count_i=0;
		for(Map.Entry<Integer,Service> entry : hm.entrySet()){
      Service service = entry.getValue();
      if(service.service_provider_id.equals(utility.user_id()) && service.service_status.equalsIgnoreCase("inactive")){
        pw.print("<tr><td><a href='ShowServiceSP?service_id="+service.service_id+"' >"+service.service_id+"</a></td><td>"+service.service_name+"</td><td>"+service.service_category_super+"</td><td>"+service.service_category_sub+"</td><td>"+service.service_rate+"</td><td>"+service.service_admin_status+"</td></tr>");
				count_i+=1;
      }
    }
		if(count_i==0){
			pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No inactive services found </h3></div>");
		}
    pw.print("</table>");
    pw.print("</div></div></div>");

	utility.printHtml("Footer.html");

	}

}
