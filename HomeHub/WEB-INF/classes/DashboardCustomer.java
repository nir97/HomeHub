import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import javax.servlet.*;

@WebServlet("/DashboardCustomer")


public class DashboardCustomer extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

		if(utility.isLoggedin() && utility.user_type().equals("customer")) {
		  if(!utility.user_info_set()) {
			response.sendRedirect("MoreInfo");
			return;
		  } else {
					displayDashboardCustomerPage(request, response, pw);
		  }
		} else {
			response.sendRedirect("Login");
			return;
		}
	}

	protected void displayDashboardCustomerPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw)throws ServletException, IOException {
		Utilities utility = new Utilities(request,pw);
		utility.printHtml("Head.html");
		utility.printCHeader();
		utility.printCLeftNav();

		RequestDispatcher rd=request.getRequestDispatcher("DealMatches");
		rd.include(request,response);

		String cat_super = request.getParameter("cat_super");
		String cat_sub = request.getParameter("cat_sub");
		Integer service_id = -1;

		try{
			service_id = Integer.parseInt(request.getParameter("service_id"));
		}catch(Exception e){

		}

		HashMap<Integer, Service> hm_s = MySqlDataStoreUtilities.getServices();
		HashMap<String, User> hm_u = MySqlDataStoreUtilities.getUsers();

		if(hm_s==null || hm_u==null){
			pw.print("<h4 style='margin-left:5%;color:red;'>MySQL server is not up and running.</h4>");
		}else{
			User user = hm_u.get(utility.user_id());
			HashMap<Integer, Service> unsorted_hm = new HashMap();
			LinkedHashMap<Integer, Service> sorted_hm = new LinkedHashMap();

			for(Map.Entry<Integer, Service> entry : hm_s.entrySet()){
				double dis = getDistanceFromLatLong(user.user_lat, user.user_long, hm_u.get(entry.getValue().service_provider_id).user_lat, hm_u.get(entry.getValue().service_provider_id).user_long);
				if(entry.getValue().service_status.equalsIgnoreCase("active") && dis <= 10.0){
					if(service_id!=-1){
						if(entry.getValue().service_id == service_id){
							unsorted_hm.put(entry.getKey(), entry.getValue());
						}
					}else{
						if(cat_super != null && !cat_super.isEmpty()){
							if(cat_super.equalsIgnoreCase(entry.getValue().service_category_super)){
								if(cat_sub != null && !cat_sub.isEmpty()){
									if(cat_sub.equalsIgnoreCase(entry.getValue().service_category_sub)){
										unsorted_hm.put(entry.getKey(), entry.getValue());
									}
								}else{
									unsorted_hm.put(entry.getKey(), entry.getValue());
								}
							}
						}else{
							unsorted_hm.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}

			Comparator<Map.Entry<Integer, Service>> valueComparator = (o1, o2) -> Double.compare(getDistanceFromLatLong(user.user_lat, user.user_long, hm_u.get(o1.getValue().service_provider_id).user_lat, hm_u.get(o1.getValue().service_provider_id).user_long), getDistanceFromLatLong(user.user_lat, user.user_long, hm_u.get(o2.getValue().service_provider_id).user_lat, hm_u.get(o2.getValue().service_provider_id).user_long));
			unsorted_hm.entrySet().stream().sorted(valueComparator).forEachOrdered(x -> sorted_hm.put(x.getKey(), x.getValue()));

			pw.print("<div class='container-fluid text-center'>");
			pw.print("<div class='row content'>");
			pw.print("<div class='col-sm-4 text-left w3-card w3-round w3-white cust-search-res' id='user-profile-sec'>");
			pw.print("<div class='w3-container '>");

			for(Map.Entry<Integer, Service> entry : sorted_hm.entrySet()){
				pw.print("<div id='item_"+entry.getKey()+"' class='search-result' onmouseover='show_marker("+entry.getKey()+")''>");
				double dis = getDistanceFromLatLong(user.user_lat, user.user_long, hm_u.get(entry.getValue().service_provider_id).user_lat, hm_u.get(entry.getValue().service_provider_id).user_long);
				pw.print("<hr><p><b>Distance from you : " + dis + " miles</b></p>");
				pw.print("<p> Provider Name : "+hm_u.get(entry.getValue().service_provider_id).user_name+" </p>");
				pw.print("<p> Provider Phone : "+hm_u.get(entry.getValue().service_provider_id).user_phone+" </p>");
				String address = hm_u.get(entry.getValue().service_provider_id).user_street1 + ", " + hm_u.get(entry.getValue().service_provider_id).user_street2 + ", " + hm_u.get(entry.getValue().service_provider_id).user_city + ", " + hm_u.get(entry.getValue().service_provider_id).user_state + " " + hm_u.get(entry.getValue().service_provider_id).user_zip + ", " + hm_u.get(entry.getValue().service_provider_id).user_country;
				pw.print("<p> Provider Address : "+address+"</p>");
				pw.print("<p> Serivce Name : "+entry.getValue().service_name+" </p>");
				pw.print("<p> Serivce Category : "+entry.getValue().service_category_super+" </p>");
				pw.print("<p> Serivce Sub-Category : "+entry.getValue().service_category_sub+" </p>");
				pw.print("<p> Serivce Description : "+entry.getValue().service_description+" </p>");
				pw.print("<p> Serivce Rate : "+entry.getValue().service_rate+" </p>");

				String imgs[] = entry.getValue().service_images.split(";");
				pw.print("<div id='myCarousel_"+entry.getKey()+"' class='carousel slide' data-ride='carousel'>");
				pw.print("<ol class='carousel-indicators'>");

				for(int i=0;i<imgs.length;i++){
				  String img = imgs[i];
				  if(i==0){
					pw.print("<li data-target='#myCarousel_"+entry.getKey()+"' data-slide-to='"+i+"' class='active'></li>");
				  }else{
					pw.print("<li data-target='#myCarousel_"+entry.getKey()+"' data-slide-to='"+i+"'></li>");
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

				pw.print("<a class='left carousel-control' href='#myCarousel_"+entry.getKey()+"' data-slide='prev'><span class='glyphicon glyphicon-chevron-left'></span><span class='sr-only'>Previous</span></a>");
				pw.print("<a class='right carousel-control' href='#myCarousel_"+entry.getKey()+"' data-slide='next'><span class='glyphicon glyphicon-chevron-right'></span><span class='sr-only'>Next</span></a>");

				pw.print("</div>");

				pw.print("<form method='get' action='ShowServiceC'>");
				pw.print("<input type='hidden' name='service_id' value='"+entry.getValue().service_id+"'>");
				pw.print("<input type='submit' class='w3-button w3-block w3-green w3-section cust-sp-book-now' value='View Details'/>");
				pw.print("</form>");
				pw.print("<hr>");
				pw.print("</div>");

			}

			pw.print("</div>");
			pw.print("</div>");

			pw.print("<div class='col-sm-4 text-left w3-card w3-round w3-white user-profile-sec' id='user-profile-map-sec'>");
      pw.print("<div class='w3-container '>");
      pw.print("<div id='google_map' style='width:100%;height:670px;'></div>");
      pw.print("<script>");
			pw.print("var map = null;");
			pw.print("var markers = {};");
			pw.print("function my_map() {");
      pw.print("var map_prop= {center:new google.maps.LatLng("+user.user_lat+","+user.user_long+"),zoom:10,};");
      pw.print("map = new google.maps.Map(document.getElementById('google_map'),map_prop);");
      pw.print("var marker = new google.maps.Marker({position: new google.maps.LatLng("+user.user_lat+","+user.user_long+")});");
      pw.print("marker.setMap(map);");
			for(Map.Entry<Integer, Service> entry : sorted_hm.entrySet()){
				pw.print("markers["+entry.getKey()+"] = new google.maps.Marker({position: new google.maps.LatLng("+hm_u.get(entry.getValue().service_provider_id).user_lat+","+hm_u.get(entry.getValue().service_provider_id).user_long+"),icon: {url: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'}});");
				pw.print("google.maps.event.addListener(markers["+entry.getKey()+"], 'click', function() {for(var k in markers){markers[k].setIcon('https://maps.google.com/mapfiles/ms/icons/blue-dot.png');}markers["+entry.getKey()+"].setMap(null);markers["+entry.getKey()+"].setIcon('https://maps.google.com/mapfiles/ms/icons/green-dot.png');markers["+entry.getKey()+"].setMap(map);document.getElementById('item_"+entry.getKey()+"').scrollIntoView();});");
				pw.print("markers["+entry.getKey()+"].setMap(map);");
			}
      pw.print("const my_circle = new google.maps.Circle({strokeColor: '#FF0000',strokeOpacity: 0.8,strokeWeight: 2,fillColor: '#FF0000',fillOpacity: 0.35,map,center: new google.maps.LatLng("+user.user_lat+","+user.user_long+"),radius: 10 * 1609.34,});");
      pw.print("}");
			pw.print("function show_marker(key){");
			pw.print("for(var k in markers){markers[k].setIcon('https://maps.google.com/mapfiles/ms/icons/blue-dot.png');}");
			pw.print("markers[key].setMap(null);");
			pw.print("markers[key].setIcon('https://maps.google.com/mapfiles/ms/icons/green-dot.png');");
			pw.print("markers[key].setMap(map);");
			pw.print("var bounds = new google.maps.LatLngBounds();");
			pw.print("bounds.extend(markers[key].getPosition());");
			pw.print("bounds.extend(new google.maps.LatLng("+user.user_lat+","+user.user_long+"));");
			pw.print("map.fitBounds(bounds);");
			pw.print("}");


      pw.print("</script>");
      pw.print("<script src='https://maps.googleapis.com/maps/api/js?key=AIzaSyDv_ayFqZGszuIOFKXl15KDnMUbzrK846c&callback=my_map'></script>");
      pw.print("</div>");
      pw.print("</div>");

			pw.print("</div>");
			pw.print("</div>");

		}

		utility.printHtml("Footer.html");
	}

	private static double getDistanceFromLatLong(double lat1, double long1, double lat2, double long2) {
	  double R = 6371.0;
	  double dLat = deg2rad(lat2-lat1);
	  double dLong = deg2rad(long2-long1);
	  double a =
	    Math.sin(dLat/2.0) * Math.sin(dLat/2.0) +
	    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
	    Math.sin(dLong/2.0) * Math.sin(dLong/2.0)
	    ;
	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	  double d = R * c * 0.621371;
	  return d;
	}

	private static double deg2rad(double deg) {
	  return deg * (Math.PI/180);
	}

}
