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

@WebServlet("/Profile")


public class Profile extends HttpServlet {

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

		if(utility.isLoggedin() && (utility.user_type().equals("customer") || utility.user_type().equals("service_provider"))) {
      if(!utility.user_info_set()) {
        response.sendRedirect("MoreInfo");
        return;
      } else {
				displayProfilePage(request, response, pw);
      }
		} else {
			response.sendRedirect("Login");
			return;
		}

	}

	protected void displayProfilePage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);
    HttpSession session = request.getSession(true);

    utility.printHtml("Head.html");
    if(utility.user_type().equalsIgnoreCase("customer")){
      utility.printCHeader();
      utility.printCLeftNav();
    }else if(utility.user_type().equalsIgnoreCase("service_provider")){
      utility.printSPHeader();
      utility.printSPLeftNav();
    }

    HashMap<String, User> hm = MySqlDataStoreUtilities.getUsers();

    if(hm!=null){
      User user = hm.get(utility.user_id());

      pw.print("<div class='container-fluid text-center'>");
      pw.print("<div class='row content'>");

      pw.print("<div class='col-sm-3 text-left w3-card w3-round w3-white user-profile-sec' id='user-profile-sec'>");
      pw.print("<div class='w3-container '>");
      pw.print("<h4 class='w3-center'>User Profile Information</h4>");
      pw.print("<p class='w3-center'><img src='images/profile/avatar3.png' class='w3-circle' style='height:106px;width:106px' alt='Avatar'></p>");
      pw.print("<hr>");
			if(session.getAttribute("profile_msg") != null) {
				pw.print("<h4 style='margin-left:5%;color:green;'>" + session.getAttribute("profile_msg") + "</h4>");
				session.removeAttribute("profile_msg");
			}
      pw.print("<p><i class='fa fa-pencil fa-fw w3-margin-right w3-text-theme'></i>"+user.user_name+"</p>");
      pw.print("<p><i class='fa fa-paper-plane fa-fw w3-margin-right w3-text-theme'></i>"+user.user_email+"</p>");
      pw.print("<p><i class='fa fa-spinner fa-fw w3-margin-right w3-text-theme'></i>"+user.user_age+"</p>");
      pw.print("<p><i class='fa fa-briefcase fa-fw w3-margin-right w3-text-theme'></i>"+user.user_occupation+"</p>");
      String address = user.user_street1 + ", " + user.user_street2 + ", " + user.user_city + ", " + user.user_state + " " + user.user_zip + ", " + user.user_country;
      pw.print("<p><i class='fa fa-home fa-fw w3-margin-right w3-text-theme'></i>"+address+"</p>");
      pw.print("<p><i class='fa fa-male fa-fw w3-margin-right w3-text-theme'></i>"+user.user_gender+"</p>");
      pw.print("<p><i class='fa fa-phone fa-fw w3-margin-right w3-text-theme'></i>"+user.user_phone+"</p>");
      pw.print("<p><i class='fa fa-birthday-cake fa-fw w3-margin-right w3-text-theme'></i>"+user.user_birthdate+"</p>");
      pw.print("<form method='get' action='MoreInfo'><input type='submit' class='w3-button w3-block w3-green w3-section' value='Update Information'/></form>");
      pw.print("</div>");
      pw.print("</div>");

      pw.print("<div class='col-sm-4 text-left w3-card w3-round w3-white user-profile-map-sec' id='user-profile-map-sec'>");
      pw.print("<div class='w3-container '>");
      pw.print("<div id='google_map' style='width:100%;height:670px;'></div>");
      pw.print("<script>");
      pw.print("function my_map() {");
      pw.print("var map_prop= {center:new google.maps.LatLng("+user.user_lat+","+user.user_long+"),zoom:10,};");
      pw.print("var map = new google.maps.Map(document.getElementById('google_map'),map_prop);");
      pw.print("var marker = new google.maps.Marker({position: new google.maps.LatLng("+user.user_lat+","+user.user_long+")});");
      pw.print("marker.setMap(map);");
      pw.print("const my_circle = new google.maps.Circle({strokeColor: '#FF0000',strokeOpacity: 0.8,strokeWeight: 2,fillColor: '#FF0000',fillOpacity: 0.35,map,center: new google.maps.LatLng("+user.user_lat+","+user.user_long+"),radius: 10 * 1609.34,});");
      pw.print("}");
      pw.print("</script>");
      pw.print("<script src='https://maps.googleapis.com/maps/api/js?key=AIzaSyDv_ayFqZGszuIOFKXl15KDnMUbzrK846c&callback=my_map'></script>");
      pw.print("</div>");
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
