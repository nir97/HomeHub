import java.io.IOException;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DealMatches")

public class DealMatches extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

    ArrayList<String> deals = new ArrayList();

		try{
		  String TOMCAT_HOME = System.getProperty("catalina.home");
		  BufferedReader reader = new BufferedReader(new FileReader (new File(TOMCAT_HOME+"\\webapps\\HomeHub\\DealMatches.txt")));
		  String line = reader.readLine();
		  while(line != null){
			deals.add(line);
			line = reader.readLine();
		  }
		}catch(Exception e){

		}

		HashMap<String, ArrayList<String>> cats = MySqlDataStoreUtilities.getCategories();

		pw.print("<div id='current-deals-div'>");
		pw.write("<h2 class='w3-hide-small' style='text-align: left'><b><u>Current Deals</u></b></h2>");

    Random ran = new Random();
    int n = 3;

		if(cats != null){
			if(deals.size() > 0){
	      for(int i=0;i<n;i++){
	        int ind = ran.nextInt(deals.size());
	        String deal = deals.get(ind);
	        deals.remove(ind);
					String href = "DashboardCustomer?";
					for(Map.Entry<String, ArrayList<String>> entry : cats.entrySet()){
						String super_cat = entry.getKey();
						if(deal.toLowerCase().indexOf(super_cat.toLowerCase()) != -1){
							href += "cat_super="+super_cat;
							for(String sub_cat : entry.getValue()){
								if(deal.toLowerCase().indexOf(super_cat.toLowerCase()) != -1){
									href += "&cat_sub="+sub_cat;
									break;
								}
							}
							break;
						}
					}
	        pw.print("<a href='"+href+"'><h4>"+deal+"</h4></a>");
	      }
	    }else{
	      pw.print("<h3 align='center'>No Offers Found</h3>");
			}
		}else{
			pw.print("<h3 align='center'>MySQL server is not up and running.</h3>");
		}

    pw.print("</div>");
	}
}
