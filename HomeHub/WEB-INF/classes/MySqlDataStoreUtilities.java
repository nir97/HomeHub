import java.sql.*;
import java.util.*;
import java.text.*;

public class MySqlDataStoreUtilities {

	static Connection conn = null;

	public static void getConnection() {
		try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_hub","root","root");
		}
		catch(Exception e) {
			System.out.println("ERROR GETTING MYSQL CONNECTION : " + e.toString());
		}
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

	public static boolean insertUser(User user) {
		try {
			getConnection();
			String insertUserQuery = "INSERT INTO Users" +
			"(user_id,user_pswd,user_type,user_info_set,user_email,user_name,user_gender,user_birthdate,user_age,user_occupation,user_phone,user_street1,user_street2,user_city,user_state,user_zip,user_country,user_lat,user_long) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertUserQuery);
			pst.setString(1,user.user_id);
			pst.setString(2,user.user_pswd);
			pst.setString(3,user.user_type);
			pst.setBoolean(4,user.user_info_set);
			pst.setString(5,user.user_email);
			pst.setString(6,user.user_name);
			pst.setString(7,user.user_gender);
			pst.setString(8,user.user_birthdate);
			pst.setInt(9,user.user_age);
			pst.setString(10,user.user_occupation);
			pst.setString(11,user.user_phone);
			pst.setString(12,user.user_street1);
			pst.setString(13,user.user_street2);
			pst.setString(14,user.user_city);
			pst.setString(15,user.user_state);
			pst.setString(16,user.user_zip);
			pst.setString(17,user.user_country);
			pst.setDouble(18,user.user_lat);
			pst.setDouble(19,user.user_long);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR INSERTING USER : " + e.toString());
			return false;
		}
	}

	public static boolean updateUser(User user) {
		try {
			getConnection();
			String updateUserQuery = "UPDATE Users SET " +
			"user_pswd=?,user_type=?,user_info_set=?,user_email=?,user_name=?,user_gender=?,user_birthdate=?,user_age=?,user_occupation=?,user_phone=?,user_street1=?,user_street2=?,user_city=?,user_state=?,user_zip=?,user_country=?,user_lat=?,user_long=? " +
			"WHERE user_id=?;";
			PreparedStatement pst = conn.prepareStatement(updateUserQuery);
			pst.setString(1,user.user_pswd);
			pst.setString(2,user.user_type);
			pst.setBoolean(3,user.user_info_set);
			pst.setString(4,user.user_email);
			pst.setString(5,user.user_name);
			pst.setString(6,user.user_gender);
			pst.setString(7,user.user_birthdate);
			pst.setInt(8,user.user_age);
			pst.setString(9,user.user_occupation);
			pst.setString(10,user.user_phone);
			pst.setString(11,user.user_street1);
			pst.setString(12,user.user_street2);
			pst.setString(13,user.user_city);
			pst.setString(14,user.user_state);
			pst.setString(15,user.user_zip);
			pst.setString(16,user.user_country);
			pst.setDouble(17,user.user_lat);
			pst.setDouble(18,user.user_long);
			pst.setString(19,user.user_id);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR UPDATING USER : " + e.toString());
			return false;
		}
	}

	public static boolean insertCategory(String super_cat, String sub_cat){
		try {
			getConnection();
			String insertCatQuery = "INSERT INTO Categories" +
			"(category_super,category_sub) " +
			"VALUES (?,?);";
			PreparedStatement pst = conn.prepareStatement(insertCatQuery);
			pst.setString(1,super_cat);
			pst.setString(2,sub_cat);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR INSERTING CAT : " + e.toString());
			return false;
		}
	}

	public static boolean deleteCategory(String super_cat, String sub_cat){
		try {
			getConnection();
			String deleteCatQuery = "Delete from Categories " +
			"where category_super='"+super_cat+"' and category_sub='"+sub_cat+"';";
			PreparedStatement pst = conn.prepareStatement(deleteCatQuery);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR DELETING CAT : " + e.toString());
			return false;
		}
	}

	public static HashMap<String, ArrayList<String>> getCategories() {
		try {
			HashMap<String,ArrayList<String>> hm = new HashMap<String,ArrayList<String>>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectCategoriesQuery = "select * from  Categories";
			ResultSet rs = stmt.executeQuery(selectCategoriesQuery);
			while(rs.next()) {
				String category_super = rs.getString("category_super");
				String category_sub = rs.getString("category_sub");
				ArrayList<String> sub_categories = hm.getOrDefault(category_super, new ArrayList<String>());
				sub_categories.add(category_sub);
				hm.put(category_super, sub_categories);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING CATEGORIES : " + e.toString());
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

	public static boolean insertService(Service service) {
		try {
			getConnection();
			String insertServiceQuery = "INSERT INTO Services" +
			"(service_provider_id,service_name,service_category_super,service_category_sub,service_images,service_description,service_rate,service_status,service_admin_status) " +
			"VALUES (?,?,?,?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertServiceQuery);
			pst.setString(1,service.service_provider_id);
			pst.setString(2,service.service_name);
			pst.setString(3,service.service_category_super);
			pst.setString(4,service.service_category_sub);
			pst.setString(5,service.service_images);
			pst.setString(6,service.service_description);
			pst.setDouble(7,service.service_rate);
			pst.setString(8,service.service_status);
			pst.setString(9,service.service_admin_status);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR INSERTING SERVICE : " + e.toString());
			return false;
		}
	}

	public static boolean updateService(Service service) {
		try {
			getConnection();
			String updateServiceQuery = "UPDATE Services SET " +
			"service_provider_id=?,service_name=?,service_category_super=?,service_category_sub=?,service_images=?,service_description=?,service_rate=?,service_status=?,service_admin_status=? " +
			"WHERE service_id=?;";
			PreparedStatement pst = conn.prepareStatement(updateServiceQuery);
			pst.setString(1,service.service_provider_id);
			pst.setString(2,service.service_name);
			pst.setString(3,service.service_category_super);
			pst.setString(4,service.service_category_sub);
			pst.setString(5,service.service_images);
			pst.setString(6,service.service_description);
			pst.setDouble(7,service.service_rate);
			pst.setString(8,service.service_status);
			pst.setString(9,service.service_admin_status);
			pst.setInt(10,service.service_id);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR UPDATING SERVICES : " + e.toString());
			return false;
		}
	}

	public static int transactionTableSize(){
		try{
			getConnection();
			Statement stmt = conn.createStatement();
			String selectTranscSizeQuery = "select count(*) as count from transactions;";
			ResultSet rs = stmt.executeQuery(selectTranscSizeQuery);
			int size = -1;
			if(rs.next()) {
				size = Integer.parseInt(rs.getString(1));
			}
			return size;
		}catch(Exception e){
			System.out.println("ERROR GETTING TRANSACTION COUNT : " + e.toString());
			return -1;
		}
	}

	public static boolean insertPayment(Transaction transaction){
		try {
			getConnection();
			String insertPaymentQuery = "INSERT INTO transactions" +
			"(transaction_id,customer_id,customer_email,customer_name,customer_gender,customer_age,customer_phone,customer_street1,customer_street2,customer_city,customer_state,customer_zip,customer_country,customer_lat,customer_long,customer_creditcard_account_no,service_id,service_name,service_category_super,service_category_sub,service_rate,service_provider_id,service_provider_email,service_provider_name,service_provider_gender,service_provider_age,service_provider_phone,service_provider_street1,service_provider_street2,service_provider_city,service_provider_state,service_provider_zip,service_provider_country,service_provider_lat,service_provider_long,service_hours,service_total_amount, booking_date,service_date,actual_service_date,  transaction_status,service_status,is_cancelled,is_delivered_on_time) "+
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			PreparedStatement pst = conn.prepareStatement(insertPaymentQuery);
			pst.setInt(1,transaction.transaction_id);
			pst.setString(2,transaction.customer_id);
			pst.setString(3,transaction.customer_email);
			pst.setString(4,transaction.customer_name);
			pst.setString(5,transaction.customer_gender);
			pst.setInt(6,transaction.customer_age);
			pst.setString(7,transaction.customer_phone);
			pst.setString(8,transaction.customer_street1);
			pst.setString(9,transaction.customer_street2);
			pst.setString(10,transaction.customer_city);
			pst.setString(11,transaction.customer_state);
			pst.setString(12,transaction.customer_zip);
			pst.setString(13,transaction.customer_country);
			pst.setDouble(14,transaction.customer_lat);
			pst.setDouble(15,transaction.customer_long);
			pst.setInt(16,transaction.customer_creditcard_account_no);
			pst.setInt(17,transaction.service_id);
			pst.setString(18,transaction.service_name);
			pst.setString(19,transaction.service_category_super);
			pst.setString(20,transaction.service_category_sub);
			pst.setDouble(21,transaction.service_rate);
			pst.setString(22,transaction.service_provider_id);
			pst.setString(23,transaction.service_provider_email);
			pst.setString(24,transaction.service_provider_name);
			pst.setString(25,transaction.service_provider_gender);
			pst.setInt(26,transaction.service_provider_age);
			pst.setString(27,transaction.service_provider_phone);
			pst.setString(28,transaction.service_provider_street1);
			pst.setString(29,transaction.service_provider_street2);
			pst.setString(30,transaction.service_provider_city);
			pst.setString(31,transaction.service_provider_state);
			pst.setString(32,transaction.service_provider_zip);
			pst.setString(33,transaction.service_provider_country);
			pst.setDouble(34,transaction.service_provider_lat);
			pst.setDouble(35,transaction.service_provider_long);
			pst.setInt(36,transaction.service_hours);
			pst.setDouble(37,transaction.service_total_amount);
			pst.setString(38,transaction.booking_date);
			pst.setString(39,transaction.service_date);
			pst.setString(40,transaction.actual_service_date);
			pst.setString(41,transaction.transaction_status);
			pst.setString(42,transaction.service_status);
			pst.setBoolean(43,transaction.is_cancelled);
			pst.setBoolean(44,transaction.is_delivered_on_time);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR INSERTING TRANS : " + e.toString());
			return false;
		}
	}

	public static HashMap<Integer, Transaction>  getTransactions(){
		try {
			HashMap<Integer,Transaction> hm = new HashMap<Integer,Transaction>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectServiceQuery = "select * from transactions;";
			ResultSet rs = stmt.executeQuery(selectServiceQuery);
			while(rs.next()) {
				Transaction transaction = new Transaction(
													rs.getInt("transaction_id"),
													rs.getString("customer_id"),
													rs.getString("customer_email"),
													rs.getString("customer_name"),
													rs.getString("customer_gender"),
													rs.getInt("customer_age"),
													rs.getString("customer_phone"),
													rs.getString("customer_street1"),
													rs.getString("customer_street2"),
													rs.getString("customer_city"),
													rs.getString("customer_state"),
													rs.getString("customer_zip"),
													rs.getString("customer_country"),
													rs.getDouble("customer_lat"),
													rs.getDouble("customer_long"),
													rs.getInt("customer_creditcard_account_no"),
													rs.getInt("service_id"),
													rs.getString("service_name"),
													rs.getString("service_category_super"),
													rs.getString("service_category_sub"),
													rs.getDouble("service_rate"),
													rs.getString("service_provider_id"),
													rs.getString("service_provider_email"),
													rs.getString("service_provider_name"),
													rs.getString("service_provider_gender"),
													rs.getInt("service_provider_age"),
													rs.getString("service_provider_phone"),
													rs.getString("service_provider_street1"),
													rs.getString("service_provider_street2"),
													rs.getString("service_provider_city"),
													rs.getString("service_provider_state"),
													rs.getString("service_provider_zip"),
													rs.getString("service_provider_country"),
													rs.getDouble("service_provider_lat"),
													rs.getDouble("service_provider_long"),
													rs.getInt("service_hours"),
													rs.getDouble("service_total_amount"),
													rs.getString("booking_date"),
													rs.getString("service_date"),
													rs.getString("actual_service_date"),
													rs.getString("transaction_status"),
													rs.getString("service_status"),
													rs.getBoolean("is_cancelled"),
													rs.getBoolean("is_delivered_on_time"));
				hm.put(rs.getInt("transaction_id"), transaction);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING Transactions : " + e.toString());
			return null;
		}
	}

	public static HashMap<Integer, Transaction>  getTransactions(String userId){
		try {
			HashMap<Integer,Transaction> hm = new HashMap<Integer,Transaction>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectServiceQuery = "select * from transactions where customer_id='"+userId+"';";
			ResultSet rs = stmt.executeQuery(selectServiceQuery);
			while(rs.next()) {
				Transaction transaction = new Transaction(
													rs.getInt("transaction_id"),
													rs.getString("customer_id"),
													rs.getString("customer_email"),
													rs.getString("customer_name"),
													rs.getString("customer_gender"),
													rs.getInt("customer_age"),
													rs.getString("customer_phone"),
													rs.getString("customer_street1"),
													rs.getString("customer_street2"),
													rs.getString("customer_city"),
													rs.getString("customer_state"),
													rs.getString("customer_zip"),
													rs.getString("customer_country"),
													rs.getDouble("customer_lat"),
													rs.getDouble("customer_long"),
													rs.getInt("customer_creditcard_account_no"),
													rs.getInt("service_id"),
													rs.getString("service_name"),
													rs.getString("service_category_super"),
													rs.getString("service_category_sub"),
													rs.getDouble("service_rate"),
													rs.getString("service_provider_id"),
													rs.getString("service_provider_email"),
													rs.getString("service_provider_name"),
													rs.getString("service_provider_gender"),
													rs.getInt("service_provider_age"),
													rs.getString("service_provider_phone"),
													rs.getString("service_provider_street1"),
													rs.getString("service_provider_street2"),
													rs.getString("service_provider_city"),
													rs.getString("service_provider_state"),
													rs.getString("service_provider_zip"),
													rs.getString("service_provider_country"),
													rs.getDouble("service_provider_lat"),
													rs.getDouble("service_provider_long"),
													rs.getInt("service_hours"),
													rs.getDouble("service_total_amount"),
													rs.getString("booking_date"),
													rs.getString("service_date"),
													rs.getString("actual_service_date"),
													rs.getString("transaction_status"),
													rs.getString("service_status"),
													rs.getBoolean("is_cancelled"),
													rs.getBoolean("is_delivered_on_time"));
				hm.put(rs.getInt("transaction_id"), transaction);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING Transactions : " + e.toString());
			return null;
		}
	}

	public static HashMap<Integer, Transaction>  getBookingsSP(String userId){
		try {
			HashMap<Integer,Transaction> hm = new HashMap<Integer,Transaction>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectServiceQuery = "select * from transactions where service_provider_id='"+userId+"';";
			ResultSet rs = stmt.executeQuery(selectServiceQuery);
			while(rs.next()) {
				Transaction transaction = new Transaction(
													rs.getInt("transaction_id"),
													rs.getString("customer_id"),
													rs.getString("customer_email"),
													rs.getString("customer_name"),
													rs.getString("customer_gender"),
													rs.getInt("customer_age"),
													rs.getString("customer_phone"),
													rs.getString("customer_street1"),
													rs.getString("customer_street2"),
													rs.getString("customer_city"),
													rs.getString("customer_state"),
													rs.getString("customer_zip"),
													rs.getString("customer_country"),
													rs.getDouble("customer_lat"),
													rs.getDouble("customer_long"),
													rs.getInt("customer_creditcard_account_no"),
													rs.getInt("service_id"),
													rs.getString("service_name"),
													rs.getString("service_category_super"),
													rs.getString("service_category_sub"),
													rs.getDouble("service_rate"),
													rs.getString("service_provider_id"),
													rs.getString("service_provider_email"),
													rs.getString("service_provider_name"),
													rs.getString("service_provider_gender"),
													rs.getInt("service_provider_age"),
													rs.getString("service_provider_phone"),
													rs.getString("service_provider_street1"),
													rs.getString("service_provider_street2"),
													rs.getString("service_provider_city"),
													rs.getString("service_provider_state"),
													rs.getString("service_provider_zip"),
													rs.getString("service_provider_country"),
													rs.getDouble("service_provider_lat"),
													rs.getDouble("service_provider_long"),
													rs.getInt("service_hours"),
													rs.getDouble("service_total_amount"),
													rs.getString("booking_date"),
													rs.getString("service_date"),
													rs.getString("actual_service_date"),
													rs.getString("transaction_status"),
													rs.getString("service_status"),
													rs.getBoolean("is_cancelled"),
													rs.getBoolean("is_delivered_on_time"));
				hm.put(rs.getInt("transaction_id"), transaction);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING Transactions : " + e.toString());
			return null;
		}
	}

	public static HashMap<Integer, Transaction> getTransactionByID(int transactionId){

		try {
			HashMap<Integer,Transaction> hm = new HashMap<Integer,Transaction>();
			getConnection();
			Statement stmt = conn.createStatement();
			String selectServiceQuery = "select * from transactions where transaction_id='"+transactionId+"';";
			ResultSet rs = stmt.executeQuery(selectServiceQuery);
			while(rs.next()) {
				Transaction transaction = new Transaction(
													rs.getInt("transaction_id"),
													rs.getString("customer_id"),
													rs.getString("customer_email"),
													rs.getString("customer_name"),
													rs.getString("customer_gender"),
													rs.getInt("customer_age"),
													rs.getString("customer_phone"),
													rs.getString("customer_street1"),
													rs.getString("customer_street2"),
													rs.getString("customer_city"),
													rs.getString("customer_state"),
													rs.getString("customer_zip"),
													rs.getString("customer_country"),
													rs.getDouble("customer_lat"),
													rs.getDouble("customer_long"),
													rs.getInt("customer_creditcard_account_no"),
													rs.getInt("service_id"),
													rs.getString("service_name"),
													rs.getString("service_category_super"),
													rs.getString("service_category_sub"),
													rs.getDouble("service_rate"),
													rs.getString("service_provider_id"),
													rs.getString("service_provider_email"),
													rs.getString("service_provider_name"),
													rs.getString("service_provider_gender"),
													rs.getInt("service_provider_age"),
													rs.getString("service_provider_phone"),
													rs.getString("service_provider_street1"),
													rs.getString("service_provider_street2"),
													rs.getString("service_provider_city"),
													rs.getString("service_provider_state"),
													rs.getString("service_provider_zip"),
													rs.getString("service_provider_country"),
													rs.getDouble("service_provider_lat"),
													rs.getDouble("service_provider_long"),
													rs.getInt("service_hours"),
													rs.getDouble("service_total_amount"),
													rs.getString("booking_date"),
													rs.getString("service_date"),
													rs.getString("actual_service_date"),
													rs.getString("transaction_status"),
													rs.getString("service_status"),
													rs.getBoolean("is_cancelled"),
													rs.getBoolean("is_delivered_on_time"));
				hm.put(rs.getInt("transaction_id"), transaction);
			}
			return hm;
		} catch(Exception e) {
			System.out.println("ERROR GETTING Transactions : " + e.toString());
			return null;
		}
	}

	public static boolean updateBookingDateSP(int transactionId, String date){
		try {
			Random ran = new Random();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date service_date = sdf.parse(date);
			Calendar c1 = Calendar.getInstance();
      c1.setTime(service_date);
			date = sdf1.format(c1.getTime());
			c1.add(Calendar.DATE, ran.nextInt(2));
			String actual_service_date = sdf1.format(c1.getTime());

			getConnection();
			String updateUserQuery = "UPDATE transactions SET " +
			"service_date=?, actual_service_date=?, service_status=?, is_delivered_on_time=? " +
			"WHERE transaction_id=?;";
			PreparedStatement pst = conn.prepareStatement(updateUserQuery);
			pst.setString(1,date);
			pst.setString(2,actual_service_date);
			pst.setString(3,"accepted");
			pst.setBoolean(4,date.equalsIgnoreCase(actual_service_date));
			pst.setInt(5,transactionId);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR UPDATING Transaction : " + e.toString());
			return false;
		}
	}

	public static boolean cancelBookingSP(int transactionId){
		try {
			getConnection();
			String updateUserQuery = "UPDATE transactions SET " +
			"service_date=?, actual_service_date=?, service_status=?, is_delivered_on_time=? " +
			"WHERE transaction_id=?;";
			PreparedStatement pst = conn.prepareStatement(updateUserQuery);
			pst.setString(1,"");
			pst.setString(2,"");
			pst.setString(3,"rejected");
			pst.setBoolean(4,false);
			pst.setInt(5,transactionId);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR UPDATING Transaction : " + e.toString());
			return false;
		}
	}

	public static boolean cancelBookingC(int transactionId){
		try {
			getConnection();
			String updateUserQuery = "UPDATE transactions SET " +
			"is_cancelled=? " +
			"WHERE transaction_id=?;";
			PreparedStatement pst = conn.prepareStatement(updateUserQuery);
			pst.setBoolean(1,true);
			pst.setInt(2,transactionId);
			pst.execute();
			return true;
		} catch(Exception e) {
			System.out.println("ERROR UPDATING Transaction : " + e.toString());
			return false;
		}
	}

		public static ArrayList<MostSoldZip> topMostSoldZip(){
			try{
				ArrayList<MostSoldZip> most_sold_zip = new ArrayList<MostSoldZip>();
				getConnection();
				int limit = 5;
				Statement stmt = conn.createStatement();
				String selectMSZQuery = "select customer_zip as zip, count(*) as count from transactions where is_cancelled=0 and service_status='accepted' group by customer_zip order by count desc limit "+limit+";";
				ResultSet rs = stmt.executeQuery(selectMSZQuery);
				while(rs.next()) {
					String zip_code = rs.getString(1);
					int count = rs.getInt(2);
					MostSoldZip msz = new MostSoldZip(zip_code, count);
					most_sold_zip.add(msz);
				}
				return most_sold_zip;
			}catch(Exception e){
				System.out.println("ERROR GETTING MSZ : " + e.toString());
				return null;
			}
	}

	public static ArrayList <MostSold> topMostSoldProducts(){
		try{
			ArrayList<MostSold> most_sold = new ArrayList<MostSold>();
			getConnection();
			int limit = 5;
			Statement stmt = conn.createStatement();
			String selectMSQuery = "select service_id, count(*) as count from transactions where is_cancelled=0 and service_status='accepted' group by service_id order by count desc limit "+limit+";";
			ResultSet rs = stmt.executeQuery(selectMSQuery);
			while(rs.next()) {
				int service_id = rs.getInt(1);
				int count = rs.getInt(2);
				MostSold ms = new MostSold(service_id, count);
				most_sold.add(ms);
			}
			return most_sold;
		}catch (Exception e){
			System.out.println("ERROR GETTING MS : " + e.toString());
			return null;
		}
	}

}
