import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/Trending")

public class Trending extends HttpServlet {

  protected void displayTrending(HttpServletRequest request,HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {
    response.setContentType("text/html");
    Utilities utility = new Utilities(request,pw);
	String userType = utility.user_type();
    pw.write("<div class='w3-main' style='margin-left:250px'>");
    pw.write("<div class='w3-container w3-text-grey' id='all_content'>");
    pw.write("<h1 class='w3-hide-small' id='trendingHeading'>Trending</h1>");
    pw.write("</div>");

    ArrayList<MostSold> most_sold = MySqlDataStoreUtilities.topMostSoldProducts();
		ArrayList<MostSoldZip> most_sold_zip = MySqlDataStoreUtilities.topMostSoldZip();
		ArrayList<BestRated> best_rated = MongoDBDataStoreUtilities.topBestRatedProducts();
    HashMap<Integer, Service> service_map = MySqlDataStoreUtilities.getServices();
    HashMap<String,User> users = MySqlDataStoreUtilities.getUsers();

    if(users!=null && service_map!=null && best_rated != null && most_sold_zip!=null && most_sold!=null){
      pw.write("<center><div><div class='w3-container w3-text-grey' id='all_content'>");
			pw.write("<h2 class='w3-hide-small' id='trendingHeading'>Best Rated Services</h2>");
			pw.write("</div>");

      		pw.print("<div class='entry' id='trendingDiv'><table id='bestseller' style='color:black'>");
  				for(BestRated br : best_rated) {
            int service_id = br.service_id;
  					Service s = service_map.get(br.service_id);
  					if(s!=null){
					    pw.print("<div id='item_"+service_id+"' class='search-result mySlides trendingSearchResdiv'>");
      				pw.print("<p id='trendingSearchResdiv'> Provider Name : "+users.get(s.service_provider_id).user_name+" </p>");
      				pw.print("<p id='trendingSearchResdiv'> Serivce Name : "+s.service_name+" </p>");
      				pw.print("<p id='trendingSearchResdiv'> Serivce Category : "+s.service_category_super+" </p>");
      				pw.print("<p id='trendingSearchResdiv'> Serivce Sub-Category : "+s.service_category_sub+" </p>");
      				pw.print("<p id='trendingSearchResdiv'> Serivce Description : "+s.service_description+" </p>");
      				pw.print("<p id='trendingSearchResdiv'> Serivce Rate : "+s.service_rate+" </p>");
					pw.print("<p id='trendingSearchResdiv'> Avgerage Rating : "+br.review_rating+" </p>");
              if(userType!=null && !userType.equalsIgnoreCase("customer")){

              }else{
                pw.print("<form method='get' action='ShowServiceC'>");
        				pw.print("<input id='trendingSearchResdiv' type='hidden' name='service_id' value='"+s.service_id+"'>");
                pw.print("<input id='trendingSearchResdivSub' type='submit' class='w3-button w3-block w3-green w3-section cust-sp-book-now' value='View Details'/>");
        				pw.print("</form>");
              }
      				pw.print("<hr>");
      				pw.print("</div>");
  					}
  				}
          pw.print("</table></div></div></center>");

      pw.write("<center><div><div class='w3-container w3-text-grey' id='all_content'>");
      pw.write("<h2 class='w3-hide-small' id='trendingHeading'>Most Sold Zipcodes</h2>");
      pw.write("</div>");

      pw.print("<div class='entry'><table id='bestseller' style='border: 2px solid #000000;border-radius: 12px;padding: 10px;'>");
  		pw.print("<tr><td style='padding: 10px;border-bottom: 2px solid #000000;'></td><td style='padding: 10px;border-bottom: 2px solid #000000;'><b>Zipcode</b></td><td style='padding: 10px;border-bottom: 2px solid #000000;'><b>Selling Counts</b></td></tr>");
  		int index=0;
  		for(MostSoldZip msz : most_sold_zip) {
  			index++;
  			pw.print("<tr>");
				pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>"+index+"). </td>");
  			pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>"+msz.zip_code+"</td>");
  			pw.print("<td style='padding: 10px;border-bottom: 2px solid #000000;'>"+msz.count+"</td>");
  			pw.print("</tr>");
			}
  		pw.print("</table></div></div></center>");

      pw.write("<center><div><div class='w3-container w3-text-grey' id='all_content'>");
			pw.write("<h2 class='w3-hide-small' id='trendingHeading'>Most Sold Services</h2>");
			pw.write("</div>");

      pw.print("<div class='entry'><table id='bestseller'>");
      for(MostSold ms : most_sold) {
        int service_id = ms.service_id;
        Service s = service_map.get(ms.service_id);
        if(s!=null){
          pw.print("<div id='item_"+service_id+"' class='search-result mySlides'>");
          pw.print("<p  id='trendingSearchResdiv'> Provider Name : "+users.get(s.service_provider_id).user_name+" </p>");
          pw.print("<p id='trendingSearchResdiv'> Serivce Name : "+s.service_name+" </p>");
          pw.print("<p id='trendingSearchResdiv'> Serivce Category : "+s.service_category_super+" </p>");
          pw.print("<p id='trendingSearchResdiv'> Serivce Sub-Category : "+s.service_category_sub+" </p>");
          pw.print("<p id='trendingSearchResdiv'> Serivce Description : "+s.service_description+" </p>");
          pw.print("<p id='trendingSearchResdiv'> Serivce Rate : "+s.service_rate+" </p>");
          pw.print("<p id='trendingSearchResdiv'> Selling Count : "+ms.count+" </p>");
          if(userType!=null && !userType.equalsIgnoreCase("customer")){

          }else{
            pw.print("<form method='get' action='ShowServiceC'>");
            pw.print("<input id='trendingSearchResdiv' type='hidden' name='service_id' value='"+s.service_id+"'>");
            pw.print("<input id='trendingSearchResdivSub' type='submit' class='w3-button w3-block w3-green w3-section cust-sp-book-now' value='View Details'/>");
            pw.print("</form>");
          }
          pw.print("<hr>");
          pw.print("</div>");
        }
      }
      pw.print("</table></div></div></center>");

    }else{
      pw.write("<h4 style='color:red'>MySQL and MongoDB server is not up and running.</h4>");
    }

    pw.write("</div>");
  }

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);

    utility.printHtml("Head.html");
    if(!utility.isLoggedin()){
      pw.print("<body id='home-body'><!-- Navbar --><nav class='navbar navbar-default' id='home-navbar'><div class='container' id='home-container'><div class='navbar-header'><button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#myNavbar'><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span></button><a class='navbar-brand' href='Home' style='color:black; margin-left: -100px'>HomeHub</a><div class='top-left'><p style='font-size:75px; letter-spacing: 0px' > Home Hub </p><p style='font-size:35px; letter-spacing: 0px; margin-top: -30px'><b><u><i>One stop for all your needs</i></u></b></p><br><br></div></div><div class='collapse navbar-collapse' id='myNavbar'><a id='login' class='nav navbar-nav navbar-right' id='#navbar-nav' href='Trending'> Trending </a><a id='login' class='nav navbar-nav navbar-right' id='#navbar-nav' href='Login'> Login </a></div></div></nav>");
			displayTrending(request, response, pw);
    }else{
      if(utility.user_type().equals("customer")){
        utility.printCHeader();
    		utility.printCLeftNav();
        displayTrending(request, response, pw);
      }else if(utility.user_type().equals("administrator")){
        utility.printAHeader();
        displayTrending(request, response, pw);
      }else if(utility.user_type().equals("service_provider")){
        utility.printSPHeader();
        displayTrending(request, response, pw);
      }
    }
    utility.printHtml("Footer.html");
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

	}

}
