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
import javax.servlet.*;


@WebServlet("/ShowServiceA")


public class ShowServiceA extends HttpServlet {

  private String msg = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    String sid = request.getParameter("service_id");
    int service_id = Integer.parseInt(sid);
    String action = request.getParameter("action");

    HashMap<Integer,Service> hm_s = MySqlDataStoreUtilities.getServices();

    if(hm_s!=null){
      Service service = hm_s.get(service_id);
      Service us = null;
      if(action.equalsIgnoreCase("accept")){
        us = new Service(service.service_id,service.service_provider_id,service.service_name,service.service_category_super,service.service_category_sub,service.service_images,service.service_description,service.service_rate,"inactive","accepted");
      }else if(action.equalsIgnoreCase("reject")){
        us = new Service(service.service_id,service.service_provider_id,service.service_name,service.service_category_super,service.service_category_sub,service.service_images,service.service_description,service.service_rate,"inactive","rejected");
      }
      boolean ret = MySqlDataStoreUtilities.updateService(us);
      if(ret){
        response.sendRedirect("ShowServiceA?service_id="+service_id);
      }else{
        this.msg = "MySQL server is not up and running.";
      }
    }else{
      this.msg = "MySQL server is not up and running.";
    }
    displayShowServicePage(request, response, pw);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    if(utility.isLoggedin() && utility.user_type().equals("administrator")) {
			displayShowServicePage(request, response, pw);
    }else{
			response.sendRedirect("Login");
      return;
		}
	}

	protected void displayShowServicePage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);
    HttpSession session = request.getSession(true);

    utility.printHtml("Head.html");
    utility.printAHeader();
    utility.printALeftNav();

    String sid = request.getParameter("service_id");

    if(sid != null && !sid.isEmpty()){
      int service_id;
      try{
        service_id = Integer.parseInt(sid);
      }catch(Exception e){
        response.sendRedirect("ShowServicesA");
        return;
      }

      HashMap<Integer,Service> hm_s = MySqlDataStoreUtilities.getServices();
      HashMap<String,User> hm_u = MySqlDataStoreUtilities.getUsers();

      if(hm_s!=null && hm_u!=null){
        Service service = hm_s.get(service_id);
        User user = hm_u.get(service.service_provider_id);

        pw.print("<div class='container-fluid text-center'>");
        pw.print("<div class='row content' id='sp-detail-info-below-header'>");
        pw.print("<div class='col-sm-2 w3-card w3-round w3-white' id='sp-detail-info-profile-sec'>");
        pw.print("<div class='w3-container'>");
        pw.print("<h4 class='w3-center'>Service Provider Information</h4>");
        pw.print("<p class='w3-center'><img src='images/profile/avatar3.png' class='w3-circle' style='height:106px;width:106px' alt='Avatar'></p>");
        pw.print("<hr>");
        pw.print("<p><i class='fa fa-pencil fa-fw w3-margin-right w3-text-theme'></i>"+user.user_name+"</p>");
        pw.print("<p><i class='fa fa-paper-plane fa-fw w3-margin-right w3-text-theme'></i>"+user.user_email+"</p>");
        pw.print("<p><i class='fa fa-spinner fa-fw w3-margin-right w3-text-theme'></i>"+user.user_age+"</p>");
        pw.print("<p><i class='fa fa-briefcase fa-fw w3-margin-right w3-text-theme'></i>"+user.user_occupation+"</p>");
        String address = user.user_street1 + ", " + user.user_street2 + ", " + user.user_city + ", " + user.user_state + " " + user.user_zip + ", " + user.user_country;
        pw.print("<p><i class='fa fa-home fa-fw w3-margin-right w3-text-theme'></i>"+address+"</p>");
        pw.print("<p><i class='fa fa-male fa-fw w3-margin-right w3-text-theme'></i>"+user.user_gender+"</p>");
        pw.print("<p><i class='fa fa-phone fa-fw w3-margin-right w3-text-theme'></i>"+user.user_phone+"</p>");
        pw.print("<p><i class='fa fa-birthday-cake fa-fw w3-margin-right w3-text-theme'></i>"+user.user_birthdate+"</p>");
        pw.print("</div>");
        pw.print("</div>");

        pw.print("<div class='col-sm-7 w3-card w3-round w3-white' id='sp-detail-info-sec'>");
        pw.print("<div class='w3-container' id='sp-detail-info-text-sec'>");
        pw.print("<br>");
        pw.print("<table class='table table-bordered table-hover'>");
        pw.print("<tr><th id='sp-detail-info-td'>Service Name</th><td id='sp-detail-info-td'>"+service.service_name+"</td></tr>");
        pw.print("<tr><th id='sp-detail-info-td'>Category</th><td id='sp-detail-info-td'>"+service.service_category_super+"</td></tr>");
        pw.print("<tr><th id='sp-detail-info-td'>Sub-Category</th><td id='sp-detail-info-td'>"+service.service_category_sub+"</td></tr>");
        pw.print("<tr><th id='sp-detail-info-td'>Description</th><td id='sp-detail-info-td'>"+service.service_description+"</td></tr>");
        pw.print("<tr><th id='sp-detail-info-td'>Rate</th><td id='sp-detail-info-td'>"+service.service_rate+"</td></tr>");
        pw.print("<tr><th id='sp-detail-info-td'>Status</th><td id='sp-detail-info-td'>"+service.service_status+"</td></tr>");
        pw.print("<tr><th id='sp-detail-info-td'>Admin Status</th><td id='sp-detail-info-td'>"+service.service_admin_status+"</td></tr>");
        pw.print("</table>");
        pw.print("</div>");

        String imgs[] = service.service_images.split(";");
        pw.print("<div id='sp-detail-info-image-sec'>");
        pw.print("<div id='myCarousel' class='carousel slide' data-ride='carousel'>");
        pw.print("<ol class='carousel-indicators'>");
        for(int i=0;i<imgs.length;i++){
          String img = imgs[i];
          if(i==0){
            pw.print("<li data-target='#myCarousel' data-slide-to='"+i+"' class='active'></li>");
          }else{
            pw.print("<li data-target='#myCarousel' data-slide-to='"+i+"'></li>");
          }
        }
        pw.print("</ol>");
        pw.print("<div class='carousel-inner'>");
        for(int i=0;i<imgs.length;i++){
          String img = imgs[i];
          if(i==0){
            pw.print("<div class='item active'>");
          }else{
            pw.print("<div class='item'>");
          }
          pw.print("<img id='sp-detail-info-image' src='images/services/"+img+"' ></div>");
        }
        pw.print("</div>");
        pw.print("<a class='left carousel-control' href='#myCarousel' data-slide='prev'><span class='glyphicon glyphicon-chevron-left'></span><span class='sr-only'>Previous</span></a>");
        pw.print("<a class='right carousel-control' href='#myCarousel' data-slide='next'><span class='glyphicon glyphicon-chevron-right'></span><span class='sr-only'>Next</span></a>");
        pw.print("</div>");
        pw.print("</div>");
        RequestDispatcher rd2 = request.getRequestDispatcher("ReviewView?service_id="+service_id);
				rd2.include(request,response);
        pw.print("</div>");

        pw.print("<div class='col-sm-2 w3-card w3-round w3-white' id='sp-detail-info-action-sec'>");
        pw.print("<div class='w3-container' >");
        pw.print("<h4 class='w3-center'>Action</h4>");
        if(this.msg != null) {
    			pw.print("<h4 style='margin-left:5%;color:red;'>" + this.msg + "</h4>");
          this.msg = null;
    		}
        if(service.service_admin_status.equalsIgnoreCase("pending")){
          pw.print("<form action='ShowServiceA' method='post'>");
          pw.print("<input type='hidden' name='service_id' value='"+service.service_id+"'>");
          pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' name='action' value='accept' title='Accept'>");
          pw.print("<input type='submit' class='w3-button w3-block w3-red w3-section' name='action' value='reject' title='Decline'>");
          pw.print("</form>");
        }else if(service.service_admin_status.equalsIgnoreCase("accepted")){
          pw.print("<form action='ShowServiceA' method='post'>");
          pw.print("<input type='hidden' name='service_id' value='"+service.service_id+"'>");
          pw.print("<input type='submit' class='w3-button w3-block w3-red w3-section' name='action' value='reject' title='Decline'>");
          pw.print("</form>");
        }else if(service.service_admin_status.equalsIgnoreCase("rejected")){
          pw.print("<form action='ShowServiceA' method='post'>");
          pw.print("<input type='hidden' name='service_id' value='"+service.service_id+"'>");
          pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' name='action' value='accept' title='Accept'>");
          pw.print("</form>");
        }
        pw.print("</div>");
        pw.print("</div>");
        pw.print("</div>");
        pw.print("</div>");

      }else{
        pw.print("<h4 style='margin-left:5%;color:red;'>MySQL server is not up and running.</h4>");
      }
    }else{
      pw.print("<h4 style='margin-left:5%;color:red;'>Not valid service id.</h4>");
    }

		utility.printHtml("Footer.html");

	}

}
