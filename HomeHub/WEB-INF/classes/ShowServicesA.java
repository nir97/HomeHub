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

@WebServlet("/ShowServicesA")


public class ShowServicesA extends HttpServlet {

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

		if(utility.isLoggedin() && utility.user_type().equals("administrator")) {
			displayShowServicesPage(request, response, pw);
		}else{
			response.sendRedirect("Login");
			return;
		}
	}

	protected void displayShowServicesPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

			Utilities utility = new Utilities(request, pw);
			HttpSession session = request.getSession(true);

			HashMap<Integer,Service> hm = MySqlDataStoreUtilities.getServices();

			utility.printHtml("Head.html");
			utility.printAHeader();
			utility.printALeftNav();

			pw.print("<div class='container-fluid text-center'>");
			pw.print("<div class='row content'>");
			pw.print("<h3 id='sp-labels'> <u>Services Overview</u></h3>");

			pw.print("<div  class='col-sm-12' id='sp-pend-headers'>");
			pw.print("<h3 id='sp-header-text'> Pending Services</h3>");
			pw.print("</div>");
			pw.print("<div  class='col-sm-12' id='sp-tables-div'>");
			pw.print("<table class='table table-bordered table-hover' id='sp-tables'>");
			pw.print("<tr><th>Id</th><th>Name</th><th>Category</th><th>Sub-category</th></tr>");
				int count_p=0;
				for(Map.Entry<Integer,Service> entry : hm.entrySet()){
			  Service service = entry.getValue();
			  if(service.service_admin_status.equalsIgnoreCase("pending")){
				pw.print("<tr><td><a href='ShowServiceA?service_id="+service.service_id+"' >"+service.service_id+"</a></td><td>"+service.service_name+"</td><td>"+service.service_category_super+"</td><td>"+service.service_category_sub+"</td></tr>");
						count_p+=1;
			  }
			}
				if(count_p==0){
					pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No pending services found </h3></div>");
				}
			pw.print("</table>");
			pw.print("</div>");

			pw.print("<div  class='col-sm-12' id='sp-appr-headers'>");
			pw.print("<h3 id='sp-header-text'>Approved Services</h3>");
			pw.print("</div>");
			pw.print("<div  class='col-sm-12' id='sp-tables-div'>");
			pw.print("<table class='table table-bordered table-hover' id='sp-tables'>");
			pw.print("<tr><th>Id</th><th>Name</th><th>Category</th><th>Sub-category</th></tr>");
				int count_a=0;
			for(Map.Entry<Integer,Service> entry : hm.entrySet()){
			  Service service = entry.getValue();
			  if(service.service_admin_status.equalsIgnoreCase("accepted")){
				pw.print("<tr><td><a href='ShowServiceA?service_id="+service.service_id+"' >"+service.service_id+"</a></td><td>"+service.service_name+"</td><td>"+service.service_category_super+"</td><td>"+service.service_category_sub+"</td></tr>");
						count_a+=1;
			  }
			}
				if(count_a==0){
					pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No accepted services found </h3></div>");
				}
			pw.print("</table>");
			pw.print("</div>");

			pw.print("<div  class='col-sm-12' id='sp-decl-headers'>");
			pw.print("<h3 id='sp-header-text'>Declined Services</h3>");
			pw.print("</div>");
			pw.print("<div  class='col-sm-12' id='sp-tables-div'>");
			pw.print("<table class='table table-bordered table-hover' id='sp-tables'>");
			pw.print("<tr><th>Id</th><th>Name</th><th>Category</th><th>Sub-category</th></tr>");
				int count_r=0;
			for(Map.Entry<Integer,Service> entry : hm.entrySet()){
			  Service service = entry.getValue();
			  if(service.service_admin_status.equalsIgnoreCase("rejected")){
				pw.print("<tr><td><a href='ShowServiceA?service_id="+service.service_id+"' >"+service.service_id+"</a></td><td>"+service.service_name+"</td><td>"+service.service_category_super+"</td><td>"+service.service_category_sub+"</td></tr>");
						count_r+=1;
			  }
			}
				if(count_r==0){
					pw.print("<div  class='col-sm-12' id='sp-tables'><h3 id='sp-labels'> No rejected services found </h3></div>");
				}
			pw.print("</table>");
			pw.print("</div>");

			utility.printHtml("Footer.html");

	}

}
