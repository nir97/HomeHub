public class Transaction{
	int transaction_id;
	String customer_id;
	String customer_email;
	String customer_name;
	String customer_gender;
	int customer_age;
	String customer_phone;
	String customer_street1;
	String customer_street2;
	String customer_city;
	String customer_state;
	String customer_zip;
	String customer_country;
	double customer_lat;
	double customer_long;
	int customer_creditcard_account_no;
	int service_id;
	String service_name;
	String service_category_super;
	String service_category_sub;
	double service_rate;
	String service_provider_id;
	String service_provider_email;
	String service_provider_name;
	String service_provider_gender;
	int service_provider_age;
	String service_provider_phone;
	String service_provider_street1;
	String service_provider_street2;
	String service_provider_city;
	String service_provider_state;
	String service_provider_zip;
	String service_provider_country;
	double service_provider_lat;
	double service_provider_long;
	int service_hours;
	double service_total_amount; 
	String booking_date;
	String service_date;
	String actual_service_date;  
	String transaction_status;
	String service_status;
	boolean is_cancelled;
	boolean is_delivered_on_time;
	
	
	public Transaction(	int transaction_id,
						String customer_id,
						String customer_email,
						String customer_name,
						String customer_gender,
						int customer_age,
						String customer_phone,
						String customer_street1,
						String customer_street2,
						String customer_city,
						String customer_state,
						String customer_zip,
						String customer_country,
						double customer_lat,
						double customer_long,
						int customer_creditcard_account_no,	
						int service_id,
						String service_name,
						String service_category_super,
						String service_category_sub,
						double service_rate,
						String service_provider_id,
						String service_provider_email,
						String service_provider_name,
						String service_provider_gender,
						int service_provider_age,
						String service_provider_phone,
						String service_provider_street1,
						String service_provider_street2,
						String service_provider_city,
						String service_provider_state,
						String service_provider_zip,
						String service_provider_country,
						double service_provider_lat,
						double service_provider_long,
						int service_hours,
						double service_total_amount, 
						String booking_date,
						String service_date,
						String actual_service_date,  
						String transaction_status,
						String service_status,
						boolean is_cancelled,
						boolean is_delivered_on_time){
		this.transaction_id = transaction_id;
		this.customer_id = customer_id;
		this.customer_email= customer_email;
		this.customer_name=customer_name;
		this.customer_gender=customer_gender;
		this.customer_age=customer_age;
		this.customer_phone=customer_phone;
		this.customer_street1=customer_street1;
		this.customer_street2=customer_street2;
		this.customer_city=customer_city;
		this.customer_state=customer_state;
		this.customer_zip=customer_zip;
		this.customer_country=customer_country;
		this.customer_lat=customer_lat;
		this.customer_long=customer_long;
		this.customer_creditcard_account_no=customer_creditcard_account_no;
		this.service_id=service_id;
		this.service_name=service_name;
		this.service_category_super=service_category_super;
		this.service_category_sub=service_category_sub;
		this.service_rate=service_rate;
		this.service_provider_id=service_provider_id;
		this.service_provider_email=service_provider_email;
		this.service_provider_name=service_provider_name;
		this.service_provider_gender=service_provider_gender;
		this.service_provider_age=service_provider_age;
		this.service_provider_phone=service_provider_phone;
		this.service_provider_street1=service_provider_street1;
		this.service_provider_street2=service_provider_street2;
		this.service_provider_city=service_provider_city;
		this.service_provider_state=service_provider_state;
		this.service_provider_zip=service_provider_zip;
		this.service_provider_country=service_provider_country;
		this.service_provider_lat=service_provider_lat;
		this.service_provider_long=service_provider_long;
		this.service_hours=service_hours;
		this.service_total_amount=service_total_amount; 
		this.booking_date=booking_date;
		this.service_date=service_date;
		this.actual_service_date=actual_service_date;  
		this.transaction_status=transaction_status;
		this.service_status=service_status;
		this.is_cancelled=is_cancelled;
		this.is_delivered_on_time=is_delivered_on_time;
	}
	
	public Transaction(){
	}
}
