public class User {

  String user_id;
  String user_pswd;
  String user_type;
  boolean user_info_set;
  String user_email;
  String user_name;
  String user_gender;
  String user_birthdate;
  int user_age;
  String user_occupation;
  String user_phone;
  String user_street1;
  String user_street2;
  String user_city;
  String user_state;
  String user_zip;
  String user_country;
  double user_lat;
  double user_long;

	public User(
  String user_id,
  String user_pswd,
  String user_type,
  boolean user_info_set,
  String user_email,
  String user_name,
  String user_gender,
  String user_birthdate,
  int user_age,
  String user_occupation,
  String user_phone,
  String user_street1,
  String user_street2,
  String user_city,
  String user_state,
  String user_zip,
  String user_country,
  double user_lat,
  double user_long
  ) {
    this.user_id = user_id;
    this.user_pswd = user_pswd;
    this.user_type = user_type;
    this.user_info_set = user_info_set;
    this.user_email = user_email;
    this.user_name = user_name;
    this.user_gender = user_gender;
    this.user_birthdate = user_birthdate;
    this.user_age = user_age;
    this.user_occupation = user_occupation;
    this.user_phone = user_phone;
    this.user_street1 = user_street1;
    this.user_street2 = user_street2;
    this.user_city = user_city;
    this.user_state = user_state;
    this.user_zip = user_zip;
    this.user_country = user_country;
    this.user_lat = user_lat;
    this.user_long = user_long;
	}

}
