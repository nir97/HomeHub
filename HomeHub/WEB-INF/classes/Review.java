import java.io.IOException;
import java.io.*;

public class Review{
	int service_id;
	String user_id;
  String user_name;
	int user_age;
	String user_gender;
	double review_rating;
	String review_date;
	String review_text;

	public Review (
  	int service_id,
  	String user_id,
  	String user_name,
  	int user_age,
  	String user_gender,
  	double review_rating,
  	String review_date,
  	String review_text
  ){
    this.service_id = service_id;
    this.user_id = user_id;
    this.user_name = user_name;
    this.user_age = user_age;
    this.user_gender = user_gender;
    this.review_rating = review_rating;
    this.review_date = review_date;
    this.review_text = review_text;
	}
}
