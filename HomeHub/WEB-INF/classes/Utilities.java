import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet("/Utilities")


public class Utilities extends HttpServlet {

	HttpServletRequest req;
	PrintWriter pw;
	String url;
	HttpSession session;

	public Utilities(HttpServletRequest req, PrintWriter pw) {
		this.req = req;
		this.pw = pw;
		this.url = this.getFullURL();
		this.session = req.getSession(true);
	}

	public void printHtml(String file) {
		String result = HtmlToString(file);
		pw.print(result);
	}

	public String getFullURL() {
		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		url.append("/");
		return url.toString();
	}

	public String HtmlToString(String file) {
		String result = null;
		try {
			String webPage = url + file;
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		}
		catch (Exception e) {
		}
		return result;
	}

	public void login(String user_id, String user_type, boolean user_info_set) {
		session.setAttribute("user_id", String.valueOf(user_id));
		session.setAttribute("user_type", String.valueOf(user_type));
		session.setAttribute("user_info_set", String.valueOf(user_info_set));
	}

	public void logout() {
		session.removeAttribute("user_id");
		session.removeAttribute("user_type");
		session.removeAttribute("user_info_set");
	}

	public boolean isLoggedin() {
		return session.getAttribute("user_id") != null && session.getAttribute("user_type") != null && session.getAttribute("user_info_set") != null;
	}

	public String user_id() {
		return session.getAttribute("user_id") != null ? session.getAttribute("user_id").toString() : null;
	}

	public String user_type() {
		return session.getAttribute("user_type") != null ? session.getAttribute("user_type").toString() : null;
	}

	public Boolean user_info_set() {
		return session.getAttribute("user_info_set") != null ? Boolean.parseBoolean(session.getAttribute("user_info_set").toString()) : null;
	}

	public HashMap<Integer,Integer> getCustomerOrders(){
		return CartItems.items.getOrDefault(user_id(), new HashMap());
	}

	public int CartCount(){
		if(isLoggedin())
			return getCustomerOrders().size();
		return 0;
	}

	public int cartItemCount(int id){
		HashMap<Integer, Integer> cartitems = CartItems.items.getOrDefault(user_id(), new HashMap());
		CartItems.items.put(user_id(), cartitems);
		return cartitems.getOrDefault(id, 0);
	}

	public void removeCart(int id){
		HashMap<Integer, Integer> cartitems = CartItems.items.getOrDefault(user_id(), new HashMap());
		CartItems.items.put(user_id(), cartitems);
		cartitems.remove(id);
	}

	public void updateCart(int id, int count){
		HashMap<Integer, Integer> cartitems = CartItems.items.getOrDefault(user_id(), new HashMap());
		CartItems.items.put(user_id(), cartitems);
		cartitems.put(id, count);
	}

	public void printLoginHeader() {
		pw.print("<nav class='navbar navbar-default' id='home-navbar'>");
		pw.print("<div class='container' id='home-container'>");
		pw.print("<div class='navbar-header'>");
		pw.print("<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#myNavbar'>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("</button>");
		pw.print("<a class='navbar-brand' href='Home' style='color:black; margin-left: -100px'>HomeHub</a>");
		pw.print("<div class='top-left'>");
		pw.print("<p style='font-size:75px; letter-spacing: 0px' > Home Hub </p>");
		pw.print("<p style='font-size:35px; letter-spacing: 0px; margin-top: -30px'><b><u><i>One stop for all your needs</i></u></b></p>");
		pw.print("<br>");
		pw.print("<br>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("<div class='collapse navbar-collapse' id='myNavbar'>");
		pw.print("<a id='login' class='nav navbar-nav navbar-right' id='#navbar-nav' href='Registration'> Register </a>");
		pw.print("<a id='login' class='nav navbar-nav navbar-right' id='#navbar-nav' href='Home'> Home </a>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("</nav>");
	}

	public void printRegistrationHeader() {
		pw.print("<nav class='navbar navbar-default' id='home-navbar'>");
		pw.print("<div class='container' id='home-container'>");
		pw.print("<div class='navbar-header'>");
		pw.print("<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#myNavbar'>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("</button>");
		pw.print("<a class='navbar-brand' href='Home' style='color:black; margin-left: -100px'>HomeHub</a>");
		pw.print("<div class='top-left'>");
		pw.print("<p style='font-size:75px; letter-spacing: 0px' > Home Hub </p>");
		pw.print("<p style='font-size:35px; letter-spacing: 0px; margin-top: -30px'><b><u><i>One stop for all your needs</i></u></b></p>");
		pw.print("<br>");
		pw.print("<br>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("<div class='collapse navbar-collapse' id='myNavbar'>");
		pw.print("<a id='login' class='nav navbar-nav navbar-right' id='#navbar-nav' href='Login'> Login </a>");
		pw.print("<a id='login' class='nav navbar-nav navbar-right' id='#navbar-nav' href='Home'> Home </a>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("</nav>");
	}

	public void printCHeader(){
		pw.print("<body onload='init()'>");
		pw.print("<nav class='navbar navbar-inverse' id='header'>");
		pw.print("<div class='container-fluid'>");
		pw.print("<div class='navbar-header'>");
		pw.print("<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#myNavbar'>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("</button>");
		pw.print("</div>");
		pw.print("<div class='collapse navbar-collapse' id='myNavbar'>");
		pw.print("<!-- Left side elements in header -->");
		pw.print("<ul class='nav navbar-nav'>");
		pw.print("<li class='active' id='headerItem'><a href='Home'>Home</a></li>");
		pw.print("<li ><a href='Profile' id='headerItem'> Profile</a></li>");
		pw.print("<li ><a href='BookingsC' id='headerItem'>Bookings</a></li>");
		pw.print("<li ><a href='Trending' id='headerItem'>Trending</a></li>");
		pw.print("</ul>");
		pw.print("<ul>");
		pw.print("<!-- Search bar -->");
		pw.print("<div class='nav navbar-nav collapse navbar-collapse form-inline my-2 my-lg-0'>");
		pw.print("<input class='form-control mr-sm-2' id='searchBar' type='test' name='complete-field' value='' onkeyup='doCompletion()' autocomplete='off' placeholder='Search for services...' aria-label='Search'>");
		pw.print("<div id='auto-row'><table id='complete-table' class='gridtable' style='position: absolute; width: 315px;'></table></div>");
		// pw.print("<button class='btn btn-outline-success my-2 my-sm-0' type='submit' id='searchButton'><i class='fa fa-search'></i></button>");
		pw.print("</div>");
		pw.print("<!-- Right side elements in header -->");
		pw.print("<ul class='nav navbar-nav navbar-right'>");
		pw.print("<li ><a href='Cart' id='headerItem'>Cart("+CartCount()+")</a></li>");
		pw.print("<li ><a href='Logout' id='headerItem'><span class='glyphicon glyphicon-log-in'></span> Logout</a></li>");
		pw.print("</ul>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("</nav>");
	}

	public void printCLeftNav(){
		pw.print("<div class='container-fluid text-center'>");
		pw.print("<div class='row content'>");
		pw.print("<!-- Left nav bar -->");
		pw.print("<div class='col-sm-2' id='custSideBarDiv'>");
		pw.print("<a href='#' id='navbarLogoText' style='color:white; font-size: 30px; margin-top: 2px;'>HomeHub</a>");
		pw.print("<nav class='w3-sidebar w3-bar-block w3-collapse w3-top'id='navCustSideBar'>");
		pw.print("<div class='w3-dropdown-hover '>");
		pw.print(" <a href='#' class='w3-bar-item w3-button' style='color:red'>Services:</i></a>");
		pw.print("</div>");

		HashMap<String, ArrayList<String>> hm = MySqlDataStoreUtilities.getCategories();

		if(hm!=null){
			for(Map.Entry<String, ArrayList<String>> entry : hm.entrySet()){
				String category_super = entry.getKey();
				pw.print("<div class='w3-dropdown-hover'>");
				pw.print("<a href='DashboardCustomer?cat_super="+category_super+"' class='w3-bar-item w3-button'>"+category_super+"<i class='fa fa-caret-down'></i></a>");
				pw.print("<div class='w3-dropdown-content w3-card-4 w3-bar-block'>");
				ArrayList<String> categories_sub = entry.getValue();
				for(String category_sub : categories_sub){
					pw.print("<a href='DashboardCustomer?cat_super="+category_super+"&cat_sub="+category_sub+"' class='w3-bar-item w3-button'>"+category_sub+"</a>");
				}
				pw.print("</div>");
				pw.print("</div>");
			}
		}else{
			pw.print("<h4 style='margin-left:5%;color:red;'>MySQL server is not up and running.</h4>");
		}

		pw.print("</div>");
		pw.print("</nav>");
	}

	public void printSPHeader() {
		pw.print("<body><nav class='navbar navbar-inverse' style='margin-left:-10px'id='header'>");
		pw.print("<div class='container-fluid'>");
		pw.print("<div class='navbar-header'>");
		pw.print("<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#myNavbar'>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("</button>");
		pw.print("<a class='navbar-brand' href='#' style='padding-left: 40px; color: white; font-size: 25px'>HomeHub</a>");
		pw.print("</div>");
		pw.print("<div class='collapse navbar-collapse' id='myNavbar'>");
		pw.print("<ul class='nav navbar-nav'>");
		pw.print("<li class='active' id='headerItem'><a href='DashboardServiceProvider'>Home</a></li>");
		pw.print("<li ><a href='Profile' id='headerItem'>Profile</a></li>");

		pw.print("<li id='headerItem'><div class='w3-dropdown-hover w3-hide-small'>");
		pw.print("<button class='w3-button' title='Notifications1' id='spServicesButton'>Services <i class='fa fa-caret-down'></i></button>");
		pw.print("<div class='w3-dropdown-content w3-card-4 w3-bar-block'>");
		pw.print("<a href='ServiceAdd' class='w3-bar-item w3-button'>Add New Service</a>");
		pw.print("<a href='ShowServicesSP' class='w3-bar-item w3-button'>My Services</a>");
		pw.print("</div></li>");
		pw.print("<li ><a href='BookingsSP' id='headerItem'>Bookings</a></li>");
		pw.print("<li ><a href='Trending' id='headerItem'>Trending</a></li>");
		pw.print("</ul>");
		pw.print("<ul>");
		pw.print("<ul class='nav navbar-nav navbar-right'>");
		pw.print("<li ><a href='Logout' id='headerItem'><span class='glyphicon glyphicon-log-in'></span> Logout</a></li>");
		pw.print("</ul>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("</nav>");
	}

	public void printSPLeftNav() {

	}

	public void printAHeader() {
		pw.print("<body><nav class='navbar navbar-inverse' style='margin-left:-10px'id='header'>");
		pw.print("<div class='container-fluid'>");
		pw.print("<div class='navbar-header'>");
		pw.print("<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#myNavbar'>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("<span class='icon-bar'></span>");
		pw.print("</button>");
		pw.print("<a class='navbar-brand' href='#' style='padding-left: 40px; color: white; font-size: 25px'>HomeHub</a>");
		pw.print("</div>");
		pw.print("<div class='collapse navbar-collapse' id='myNavbar'>");
		pw.print("<ul class='nav navbar-nav'>");
		pw.print("<li class='active' id='headerItem'><a href='DashboardAdministrator'>Home</a></li>");
		pw.print("<li ><a href='ShowServicesA' id='headerItem'>Services</a></li>");
		pw.print("<li ><a href='CategoryADA' id='headerItem'>Add/Delete Category</a></li>");
		pw.print("<li ><a href='DataVisualization' id='headerItem'>Report</a></li>");
		pw.print("<li ><a href='Trending' id='headerItem'>Trending</a></li>");
		pw.print("</ul>");
		pw.print("<ul>");
		pw.print("<ul class='nav navbar-nav navbar-right'>");
		pw.print("<li ><a href='Logout' id='headerItem'><span class='glyphicon glyphicon-log-in'></span> Logout</a></li>");
		pw.print("</ul>");
		pw.print("</div>");
		pw.print("</div>");
		pw.print("</nav>");
	}

	public void printALeftNav() {

	}

}
