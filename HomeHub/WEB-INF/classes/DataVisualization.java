import com.google.gson.Gson;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.mongodb.AggregationOutput;


@WebServlet("/DataVisualization")
public class DataVisualization extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(request, pw);

    if(utility.isLoggedin() && utility.user_type().equals("administrator")) {
      displayReportPage(request, response, pw);
    }else{
      response.sendRedirect("Login");
      return;
    }
  }

  protected void displayReportPage(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws ServletException, IOException {

    Utilities utility = new Utilities(request, pw);

    utility.printHtml("Head.html");
    utility.printAHeader();
    utility.printALeftNav();
		
	
		pw.write("<div class='w3-container w3-text-grey' id='all_content'>");
		pw.write("<h1 class='w3-hide-small' style='color:black'><center>Data Visualization</center></h1>");
		pw.write("</div>");

		pw.write("<div class='w3-container w3-text-grey' id='all_content'>");
		pw.write("<h2 class='w3-hide-small' style='color:black'>Inventory</h2>");
		pw.write("</div>");

		pw.print("<h3><button id='btnTableServices' class='btnreview w3-button w3-black'>View Table of Services</h3>");
		pw.println("<div id='table_services' style='display:none;'></div>");

		pw.print("<h3><button id='btnChartServices' class='btnreview w3-button w3-black'>View Chart of Services</h3>");
		pw.println("<div id='chart_services' style='display:none;'></div>");

		pw.write("<div class='w3-container w3-text-grey' id='all_content'>");
		pw.write("<h2 class='w3-hide-small'  style='color:black'>Sales Report</h2>");
		pw.write("</div>");

		pw.print("<h3><button id='btnTableServiceSold' class='btnreview w3-button w3-black'>View Table of Service Sold</h3>");
		pw.println("<div id='table_service_sold' style='display:none;'></div>");

		pw.print("<h3><button id='btnChartServiceSold' class='btnreview w3-button w3-black'>View Chart of Service Sold</h3>");
		pw.println("<div id='chart_service_Sold' style='display:none;'></div>");

		pw.print("<h3><button id='btnTableDailySalesTransaction' class='btnreview w3-button w3-black'>View Table of Daily Sales Transaction</h3>");
		pw.println("<div id='table_daily_sales_transaction' style='display:none;'></div>");

		pw.println("<script type='text/javascript' src=\"https://www.gstatic.com/charts/loader.js\"></script>");
		pw.println("<script type='text/javascript' src='DataVisualization.js'></script>");


		utility.printHtml("Footer.html");

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
			String action = request.getParameter("action");
			String dataJson = null;
      if(action.equalsIgnoreCase("table_services")){
        HashMap<Integer, Service> services = MySqlDataStoreUtilities.getServices();
        HashMap<String, HashMap<String, Integer>> map_services = new HashMap();

        for(Map.Entry<Integer, Service> entry : services.entrySet()){
          Service s = entry.getValue();
          if(s.service_status.equalsIgnoreCase("active")){
            String cat_super = s.service_category_super.toLowerCase();
            String cat_sub = s.service_category_sub.toLowerCase();

            HashMap<String, Integer> map_service = map_services.getOrDefault(cat_super, new HashMap());
            map_service.put(cat_sub, map_service.getOrDefault(cat_sub, 0) + 1);
            map_services.put(cat_super, map_service);
          }
        }

        ArrayList<HashMap<String, Object>> list_services = new ArrayList();
        for(Map.Entry<String,HashMap<String, Integer>> entry : map_services.entrySet()){
          for(Map.Entry<String, Integer> entry1 : entry.getValue().entrySet()){
            HashMap<String, Object> obj = new HashMap();
            obj.put("category super", entry.getKey());
            obj.put("category sub", entry1.getKey());
            obj.put("service count", entry1.getValue());
            list_services.add(obj);
          }
        }
        dataJson = new Gson().toJson(list_services);
      }
      if(action.equalsIgnoreCase("chart_services")){
        HashMap<Integer, Service> services = MySqlDataStoreUtilities.getServices();
        HashMap<String, HashMap<String, Integer>> map_services = new HashMap();

        for(Map.Entry<Integer, Service> entry : services.entrySet()){
          Service s = entry.getValue();
          if(s.service_status.equalsIgnoreCase("active")){
            String cat_super = s.service_category_super.toLowerCase();
            String cat_sub = s.service_category_sub.toLowerCase();

            HashMap<String, Integer> map_service = map_services.getOrDefault(cat_super, new HashMap());
            map_service.put(cat_sub, map_service.getOrDefault(cat_sub, 0) + 1);
            map_services.put(cat_super, map_service);
          }
        }

        ArrayList<HashMap<String, Object>> list_services = new ArrayList();
        for(Map.Entry<String,HashMap<String, Integer>> entry : map_services.entrySet()){
          for(Map.Entry<String, Integer> entry1 : entry.getValue().entrySet()){
            HashMap<String, Object> obj = new HashMap();
            obj.put("category super", entry.getKey());
            obj.put("category sub", entry1.getKey());
            obj.put("service count", entry1.getValue());
            list_services.add(obj);
          }
        }
        dataJson = new Gson().toJson(list_services);
      }
      if(action.equalsIgnoreCase("table_service_sold")){
				HashMap<Integer, Transaction> map_transaction = MySqlDataStoreUtilities.getTransactions();
				HashMap<String, HashMap<String, Object>> map_service_transactions = new HashMap();

				for(Map.Entry<Integer, Transaction> entry : map_transaction.entrySet()){
          Transaction t = entry.getValue();
          if(t.is_cancelled == false && t.service_status.equalsIgnoreCase("accepted") && t.transaction_status.equalsIgnoreCase("approved")){
            String service_name = t.service_name.toLowerCase();
            double service_rate = t.service_rate;
            int service_hours = t.service_hours;
            double service_sales = t.service_total_amount;

            HashMap<String, Object> map_service = map_service_transactions.getOrDefault(service_name, new HashMap());
            map_service.put("name", service_name);
            map_service.put("rate", service_rate);
            map_service.put("hours", (int)map_service.getOrDefault("hours", 0) + service_hours);
            map_service.put("sales", (double)map_service.getOrDefault("sales", 0.0) + service_sales);
            map_service_transactions.put(service_name, map_service);
          }
				}

				ArrayList<HashMap<String, Object>> list_service_transaction = new ArrayList();
				for(Map.Entry<String,HashMap<String, Object>> entry : map_service_transactions.entrySet()){
					list_service_transaction.add(entry.getValue());
				}
				dataJson = new Gson().toJson(list_service_transaction);
			}
			if(action.equalsIgnoreCase("chart_service_Sold")){
        HashMap<Integer, Transaction> map_transaction = MySqlDataStoreUtilities.getTransactions();
				HashMap<String, HashMap<String, Object>> map_service_transactions = new HashMap();

        for(Map.Entry<Integer, Transaction> entry : map_transaction.entrySet()){
          Transaction t = entry.getValue();
          if(t.is_cancelled == false && t.service_status.equalsIgnoreCase("accepted") && t.transaction_status.equalsIgnoreCase("approved")){
            String service_name = t.service_name.toLowerCase();
            double service_sales = t.service_total_amount;

            HashMap<String, Object> map_service = map_service_transactions.getOrDefault(service_name, new HashMap());
            map_service.put("name", service_name);
            map_service.put("sales", (double)map_service.getOrDefault("sales", 0.0) + service_sales);
            map_service_transactions.put(service_name, map_service);
          }
        }

        ArrayList<HashMap<String, Object>> list_service_transaction = new ArrayList();
        for(Map.Entry<String,HashMap<String, Object>> entry : map_service_transactions.entrySet()){
          list_service_transaction.add(entry.getValue());
        }
        dataJson = new Gson().toJson(list_service_transaction);
			}
			if(action.equalsIgnoreCase("table_daily_sales_transaction")){
				HashMap<Integer, Transaction> map_transaction = MySqlDataStoreUtilities.getTransactions();
				HashMap<String, HashMap<String, Object>> map_daily_sales = new HashMap();

        for(Map.Entry<Integer, Transaction> entry : map_transaction.entrySet()){
          Transaction t = entry.getValue();
          if(t.is_cancelled == false && t.service_status.equalsIgnoreCase("accepted") && t.transaction_status.equalsIgnoreCase("approved")){
            String date = t.booking_date;
            double sales = t.service_total_amount;

            HashMap<String, Object> map_ds = map_daily_sales.getOrDefault(date, new HashMap());
            map_ds.put("date", date);
            map_ds.put("sales", (double)map_ds.getOrDefault("sales", 0.0) + sales);
            map_daily_sales.put(date, map_ds);
          }
        }

				ArrayList<HashMap<String, Object>> list_daily_sales = new ArrayList();
				for(Map.Entry<String,HashMap<String, Object>> entry : map_daily_sales.entrySet()){
					list_daily_sales.add(entry.getValue());
				}
				dataJson = new Gson().toJson(list_daily_sales);
			}

      response.setContentType("application/JSON");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(dataJson);

    } catch(Exception e) {
        System.out.println(e.getMessage());
    }
  }

}
