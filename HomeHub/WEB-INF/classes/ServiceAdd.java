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

@WebServlet("/ServiceAdd")


public class ServiceAdd extends HttpServlet {

	private String msg = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    String service_name = request.getParameter("service_name");
    double service_rate = Double.parseDouble(request.getParameter("service_rate"));
    String service_category_super = request.getParameter("service_category_super");
    String service_category_sub = request.getParameter("service_category_sub");
    String service_description = request.getParameter("service_description");
    String service_images = "";

    Random ran = new Random();
    int num_img = ran.nextInt(5);
    for(int i=0;i<num_img;i++){
      service_images += service_category_super + "_" + service_category_sub + "_" + ran.nextInt(5) + ".jpg";
      if(i!=num_img-1){
        service_images += ";";
      }
    }
		if(service_category_super!=null && service_category_sub!=null && !service_category_super.isEmpty() && !service_category_sub.isEmpty() && !service_category_super.equalsIgnoreCase("default") && !service_category_sub.equalsIgnoreCase("default")){
			Service service = new Service(-1,utility.user_id(),service_name,service_category_super,service_category_sub,service_images,service_description,service_rate,"inactive","pending");
	    boolean ret = MySqlDataStoreUtilities.insertService(service);
	    if(ret == false) {
	      this.msg = "MySQL server is not up and running. Please try again after some time.";
	    } else {
	      this.msg = "Your service is added successfully. Add another service!";
	    }
		}else{
			this.msg = "Please choose valid category and sub-category.";
		}

    displayServiceAddPage(request, response, pw);
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
				displayServiceAddPage(request, response, pw);
      }
		} else {
			response.sendRedirect("Login");
			return;
		}

	}

	protected void displayServiceAddPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);
    HttpSession session = request.getSession(true);

    HashMap<String, ArrayList<String>> categories = MySqlDataStoreUtilities.getCategories();

    utility.printHtml("Head.html");
    utility.printSPHeader();
    utility.printSPLeftNav();

		pw.print("<div class='col-sm-12 text-left' id='spFormContent'>");
		pw.print("<h3> Add service information </h3>");
		pw.print("<form class='form-horizontal' method='post' action='ServiceAdd' style='height:650px;'>");

    if(this.msg!=null) {
      pw.print("<h4 style='margin-left:5%;color:red;'>" + this.msg + "</h4>");
      this.msg = null;
    }

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<input type='text' class='form-control' id='service_name' placeholder='Service Name' name='service_name' required>");
		pw.print("</div></div>");

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<input type='file' class='form-control' id='service_images' accept='image/*' placeholder='Upload Image' name='service_images' multiple='true' required>");
		pw.print("</div></div>");

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<input type='number' class='form-control' id='service_rate' placeholder='Hourly rate' name='service_rate' min='0' max='100' required>");
		pw.print("</div></div>");

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<select class='js-select2' name='service_category_super'>");
		pw.print("<option value='default' selected>Please choose category</option>");
    ArrayList<String> categories_sub = new ArrayList();
    for(Map.Entry<String, ArrayList<String>> entry : categories.entrySet()){
      pw.print("<option value='" + entry.getKey() + "'>" + entry.getKey() + "</option>");
      for(String cat_sub : entry.getValue()){
        categories_sub.add(cat_sub);
      }
    }
		pw.print("</select>");
		pw.print("<div class='dropDownSelect2'></div>");
		pw.print("</div>");
		pw.print("</div>");

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<select class='js-select1' name='service_category_sub'>");
		pw.print("<option value='default' selected>Please choose sub-category</option>");
    for(String cat_sub : categories_sub){
      pw.print("<option value='" + cat_sub + "'>" + cat_sub + "</option>");
    }
		pw.print("</select>");
		pw.print("<div class='dropDownSelect1'></div>");
		pw.print("</div>");
		pw.print("</div>");

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-10'>");
		pw.print("<textarea class='form-control' id='service_description' placeholder='Your Description here...' name='service_description' required></textarea>");
		pw.print("</div></div>");

		pw.print("<div class='form-group'>");
		pw.print("<div class='col-sm-offset-2 col-sm-10'>");
		pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' style='width:200px; margin-left:-50px' value='Submit'/>");
		pw.print("</div></div>");

    pw.print("</form>");
    pw.print("</div>");
    pw.print("</div>");

		utility.printHtml("Footer.html");

	}

}
