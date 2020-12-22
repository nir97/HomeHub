import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;


public class Carousel{

	public String  carouselfeature(Utilities utility){

		HashMap<String,ArrayList<Integer>> serviceRecMap = ServiceRecommenderUtility.readFile();
		HashMap<Integer, Service> services = MySqlDataStoreUtilities.getServices();
    HashMap<String, User> users = MySqlDataStoreUtilities.getUsers();

    User user = users.get(utility.user_id());

		StringBuilder sb = new StringBuilder();
		String myCarousel = null;

		if(serviceRecMap!=null && services !=null && users!=null){

			String uid = utility.user_id();
			ArrayList<Integer> rec = serviceRecMap.getOrDefault(uid, new ArrayList());
      ArrayList<Integer> s_rec = new ArrayList();

      for(int sid : rec){
        Service s = services.get(sid);
        double dis = getDistanceFromLatLong(user.user_lat, user.user_long, users.get(s.service_provider_id).user_lat, users.get(s.service_provider_id).user_long);
        if(s.service_status.equalsIgnoreCase("active") && dis <= 10.0){
          s_rec.add(sid);
        }
      }

      rec = s_rec;

			if(rec != null){

				if(rec.size() > 0){

					sb.append("<div class='w3-container w3-text-grey' id='all_content'>");
					sb.append("<h3 class='w3-hide-small'>Recommended Services</h3>");
					sb.append("<p></p>");
					sb.append("</div>");

					sb.append("<div class='w3-content w3-display-container' style='max-width:700px;'>");

					for(int service_id : rec){
						Service s = services.get(service_id);

            sb.append("<div id='item_"+service_id+"' class='search-result mySlides'>");
    				double dis = getDistanceFromLatLong(user.user_lat, user.user_long, users.get(s.service_provider_id).user_lat, users.get(s.service_provider_id).user_long);
    				sb.append("<hr><p><b>Distance from you : " + dis + " miles</b></p>");
    				sb.append("<p> Provider Name : "+users.get(s.service_provider_id).user_name+" </p>");
    				sb.append("<p> Provider Phone : "+users.get(s.service_provider_id).user_phone+" </p>");
    				String address = users.get(s.service_provider_id).user_street1 + ", " + users.get(s.service_provider_id).user_street2 + ", " + users.get(s.service_provider_id).user_city + ", " + users.get(s.service_provider_id).user_state + " " + users.get(s.service_provider_id).user_zip + ", " + users.get(s.service_provider_id).user_country;
    				sb.append("<p> Provider Address : "+address+"</p>");
    				sb.append("<p> Serivce Name : "+s.service_name+" </p>");
    				sb.append("<p> Serivce Category : "+s.service_category_super+" </p>");
    				sb.append("<p> Serivce Sub-Category : "+s.service_category_sub+" </p>");
    				sb.append("<p> Serivce Description : "+s.service_description+" </p>");
    				sb.append("<p> Serivce Rate : "+s.service_rate+" </p>");

    				String imgs[] = s.service_images.split(";");
    				sb.append("<div id='myCarousel_"+service_id+"' class='carousel slide' data-ride='carousel'>");
    				sb.append("<ol class='carousel-indicators'>");

    				for(int i=0;i<imgs.length;i++){
    				  String img = imgs[i];
    				  if(i==0){
    					sb.append("<li data-target='#myCarousel_"+service_id+"' data-slide-to='"+i+"' class='active'></li>");
    				  }else{
    					sb.append("<li data-target='#myCarousel_"+service_id+"' data-slide-to='"+i+"'></li>");
    				  }
    				}

    				sb.append("</ol>");
    				sb.append("<div class='carousel-inner'>");
    				for(int i=0;i<imgs.length;i++){
              String img = imgs[i];
              if(i==0){
                sb.append("<div class='item active'>");
              }else{
                sb.append("<div class='item'>");
              }
              sb.append("<img id='sp-detail-info-image' src='images/services/"+img+"' ></div>");
            }
    				sb.append("</div>");

    				sb.append("<a class='left carousel-control' href='#myCarousel_"+service_id+"' data-slide='prev'><span class='glyphicon glyphicon-chevron-left'></span><span class='sr-only'>Previous</span></a>");
    				sb.append("<a class='right carousel-control' href='#myCarousel_"+service_id+"' data-slide='next'><span class='glyphicon glyphicon-chevron-right'></span><span class='sr-only'>Next</span></a>");

    				sb.append("</div>");

    				sb.append("<form method='get' action='ShowServiceC'>");
    				sb.append("<input type='hidden' name='service_id' value='"+s.service_id+"'>");
    				sb.append("<input type='submit' class='w3-button w3-block w3-green w3-section cust-sp-book-now' value='View Details'/>");
    				sb.append("</form>");
    				sb.append("<hr>");
    				sb.append("</div>");
					}


					sb.append("<button class='w3-button w3-display-left w3-black' onclick='plusDivs(-1)'>&#10094;</button>");
					sb.append("<button class='w3-button w3-display-right w3-black' onclick='plusDivs(1)'>&#10095;</button>");

					sb.append("</div>");

					sb.append("<script>");
					sb.append("var slideIndex = 0;");
					sb.append("showDivs(slideIndex);");
					sb.append("function plusDivs(n) {showDivs(slideIndex += n);}");
					sb.append("function showDivs(n) {var i;var x = document.getElementsByClassName('mySlides');if (n >= x.length) {slideIndex = 0}if (n < 0) {slideIndex = x.length-1}for (i = 0; i < x.length; i++) {x[i].style.display = 'none';}x[slideIndex].style.display = 'block';}");
					sb.append("carousel();");
					sb.append("function carousel() {var i;var x = document.getElementsByClassName('mySlides');for (i = 0; i < x.length; i++) {x[i].style.display = 'none';}slideIndex++;if (slideIndex >= x.length) {slideIndex = 0}x[slideIndex].style.display = 'block';setTimeout(carousel, 10000);}");
					sb.append("</script>");
				}else{
          sb.append("<h4 style='color:red'>There are no Recommendation for you.</h4>");
        }

			}

		}else{
			sb.append("<h4 style='color:red'>MySQL server is not up and running.</h4>");
		}

		return sb.toString();
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
