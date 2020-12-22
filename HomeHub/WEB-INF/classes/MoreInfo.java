import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;

@WebServlet("/MoreInfo")
public class MoreInfo extends HttpServlet {

  private String error_msg = null;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

    String user_id = utility.user_id();
		String user_pswd = null;
		String user_type = utility.user_type();
    Boolean user_info_set = true;

    String user_email = request.getParameter("user_email");
    String user_name = request.getParameter("user_name");
    String user_gender = request.getParameter("user_gender");
    String user_birthdate = request.getParameter("user_birthdate");
    int user_age = Integer.parseInt(request.getParameter("user_age"));
    String user_occupation = request.getParameter("user_occupation");
    String user_phone = request.getParameter("user_phone");
    String user_street1 = request.getParameter("user_street1");
    String user_street2 = request.getParameter("user_street2");
    String user_city = request.getParameter("user_city");
    String user_state = request.getParameter("user_state");
    String user_zip = request.getParameter("user_zip");
    String user_country = request.getParameter("user_country");
    double user_lat = 0.0;
    double user_long = 0.0;

    HashMap<String, User> hm = MySqlDataStoreUtilities.getUsers();
    if(hm == null) {
      this.error_msg = "MySQL server is not up and running.";
    } else {
      if(!hm.containsKey(user_id)) {
        this.error_msg = "UserID doesn't exist.";
      } else {
        user_pswd = hm.get(user_id).user_pswd;
        String address = user_street1 + ", " + user_street2 + ", " + user_city + ", " + user_state + " " + user_zip + ", " + user_country;
        try{
          String api = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&key=AIzaSyDv_ayFqZGszuIOFKXl15KDnMUbzrK846c";
          URL url = new URL(api);
          HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
          httpConnection.connect();
          int responseCode = httpConnection.getResponseCode();
          if(responseCode == 200) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(httpConnection.getInputStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/GeocodeResponse/status");
            String status = (String)expr.evaluate(document, XPathConstants.STRING);
            if(status.equals("OK")){
               expr = xpath.compile("//geometry/location/lat");
               user_lat = Double.parseDouble(String.valueOf(expr.evaluate(document, XPathConstants.STRING)));
               expr = xpath.compile("//geometry/location/lng");
               user_long = Double.parseDouble(String.valueOf(expr.evaluate(document, XPathConstants.STRING)));
               User user = new User(user_id,user_pswd,user_type,user_info_set,user_email,user_name,user_gender,user_birthdate,user_age,user_occupation,user_phone,user_street1,user_street2,user_city,user_state,user_zip,user_country,user_lat,user_long);
               boolean ret = MySqlDataStoreUtilities.updateUser(user);
               if(ret == false) {
                 this.error_msg = "MySQL server is not up and running.";
               } else {
                 utility.login(user.user_id, user.user_type, user.user_info_set);
                 HttpSession session = request.getSession(true);
       					 session.setAttribute("profile_msg", "Your profile is updated.");
                 response.sendRedirect("Profile");
                 return;
               }
            } else {
               this.error_msg = "Error getting location coordinates. Please enter valid address";
            }
          } else {
            this.error_msg = "Error getting location coordinates.";
          }
        } catch(Exception e) {
          System.out.println("ERROR PARSEING GEOCODER XML : " + e.toString());
          this.error_msg = "Error parsing data of geocoder api response";
        }
      }
    }
		displayMoreInfoPage(request, response, pw);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

		if(utility.isLoggedin() && (utility.user_type().equals("customer") || utility.user_type().equals("service_provider"))){
			displayMoreInfoPage(request, response, pw);
		} else {
			response.sendRedirect("Login");
      return;
		}

	}

  protected void displayMoreInfoPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);

    utility.printHtml("Head.html");
    if(utility.user_type().equalsIgnoreCase("customer")){
      utility.printCHeader();
      utility.printCLeftNav();

	  pw.print("<div class='col-sm-7 text-left' id='content'>");
    }else if(utility.user_type().equalsIgnoreCase("service_provider")){
      utility.printSPHeader();
      //utility.printSPLeftNav();
	  pw.print("<div class='col-sm-12 text-left' id='spFormContent'>");
    }

	pw.print("<h3> Complete the following form: </h3>");
    if(this.error_msg != null) {
      pw.print("<h4 style='margin-left:5%;color:red;'>" + this.error_msg + "</h4>");
      this.error_msg = null;
    }

    pw.print("<form class='form-horizontal' id='more_info' method='post' action='MoreInfo'>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='email' class='form-control' id='user_email' placeholder='User Email' name='user_email' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_name' placeholder='User Name' name='user_name' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<label class='form-check-label'> Gender </label>");
    pw.print("<input class='form-check-input' type='radio' name='user_gender' id='user_gender_male' value='m' checked><label> Male</label>");
    pw.print("<input class='form-check-input' type='radio' name='user_gender' id='user_gender_female' value='f'><label> Female</label>");
    pw.print("<input class='form-check-input' type='radio' name='user_gender' id='user_gender_other' value='o'><label> Other</label>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<label class='form-check-label'> User Birthday: </label>");
    pw.print("<input type='text' class='form-control' id='user_birthdate' placeholder='mm/dd/yyyy' name='user_birthdate' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='number' class='form-control' id='user_age' placeholder='User Age' name='user_age' min='0' max='100' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_occupation' placeholder='User Occupation' name='user_occupation' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_phone' placeholder='User Phone' name='user_phone' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_street1' placeholder='Address Line-1' name='user_street1' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_street2' placeholder='Address Line-2' name='user_street2' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_city' placeholder='City' name='user_city' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_state' placeholder='State' name='user_state' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_zip' placeholder='Zip' name='user_zip' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-10'>");
    pw.print("<input type='text' class='form-control' id='user_country' placeholder='Country' name='user_country' required>");
    pw.print("</div></div>");

    pw.print("<div class='form-group'>");
    pw.print("<div class='col-sm-offset-2 col-sm-10'>");
    pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section' value='Submit'/>");
    pw.print("</div></div>");

    pw.print("</form>");
    pw.print("</div></div></div>");

    utility.printHtml("Footer.html");

  }

}
