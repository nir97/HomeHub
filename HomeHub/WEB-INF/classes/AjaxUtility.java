import java.io.*;

import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.util.*;
import java.text.*;

import java.sql.*;

import java.io.IOException;
import java.io.*;



public class AjaxUtility {
	StringBuffer sb = new StringBuffer();
	boolean names_added = false;
	static Connection conn = null;
    static String message;

	public static String getConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_hub","root","root");
			message="Successfull";
			return message;
		}
		catch(SQLException e)
		{
			 message="unsuccessful";
		     return message;
		}
		catch(Exception e)
		{
			 message="unsuccessful";
		     return message;
		}
	}

	public  StringBuffer readdata(String search_id) {

		HashMap<Integer,Service> data = getServices();
    HashMap<String, User> data1 = getUsers();

 	  Iterator it = data.entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry si = (Map.Entry)it.next();
			if(si!=null) {
			      Service s=(Service)si.getValue();
            if(
              data1.get(s.service_provider_id).user_name.toLowerCase().startsWith(search_id) ||
              s.service_name.toLowerCase().startsWith(search_id) ||
              s.service_category_super.toLowerCase().startsWith(search_id) ||
              s.service_category_sub.toLowerCase().startsWith(search_id)
            ) {
             sb.append("<service>");
             sb.append("<service_id>" + s.service_id + "</service_id>");
             sb.append("<service_provider_name>" + data1.get(s.service_provider_id).user_name + "</service_provider_name>");
             sb.append("<service_name>" + s.service_name + "</service_name>");
             sb.append("<service_category_super>" + s.service_category_super + "</service_category_super>");
             sb.append("<service_category_sub>" + s.service_category_sub + "</service_category_sub>");
             sb.append("</service>");
           }
			}
    }
	  return sb;
	}

  public static HashMap<String, User> getUsers() {
		try {
			HashMap<String,User> hm = new HashMap<String,User>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectUserQuery = "select * from  Users";
			ResultSet rs = stmt.executeQuery(selectUserQuery);
			while(rs.next()) {
				User user = new User(
											rs.getString("user_id"),
											rs.getString("user_pswd"),
											rs.getString("user_type"),
											rs.getBoolean("user_info_set"),
											rs.getString("user_email"),
											rs.getString("user_name"),
											rs.getString("user_gender"),
											rs.getString("user_birthdate"),
											rs.getInt("user_age"),
											rs.getString("user_occupation"),
											rs.getString("user_phone"),
											rs.getString("user_street1"),
											rs.getString("user_street2"),
											rs.getString("user_city"),
											rs.getString("user_state"),
											rs.getString("user_zip"),
											rs.getString("user_country"),
											rs.getDouble("user_lat"),
											rs.getDouble("user_long")
										);
				hm.put(rs.getString("user_id"), user);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING USERS : " + e.toString());
			return null;
		}
	}

  public static HashMap<Integer, Service> getServices() {
		try {
			HashMap<Integer,Service> hm = new HashMap<Integer,Service>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectServiceQuery = "select * from  Services";
			ResultSet rs = stmt.executeQuery(selectServiceQuery);
			while(rs.next()) {
				Service service = new Service(
											rs.getInt("service_id"),
											rs.getString("service_provider_id"),
											rs.getString("service_name"),
											rs.getString("service_category_super"),
											rs.getString("service_category_sub"),
											rs.getString("service_images"),
											rs.getString("service_description"),
											rs.getDouble("service_rate"),
											rs.getString("service_status"),
											rs.getString("service_admin_status")
										);
				hm.put(rs.getInt("service_id"), service);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING SERVICES : " + e.toString());
			return null;
		}
	}

}
