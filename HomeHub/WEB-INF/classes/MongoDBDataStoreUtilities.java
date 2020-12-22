import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.AggregationOutput;
import java.util.*;

public class MongoDBDataStoreUtilities {

	static DBCollection myReviews;

	public static DBCollection getConnection(){
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("HomeHubReviews");
		myReviews= db.getCollection("myReviews");
		return myReviews;
	}

	public static boolean insertReview(Review r){
		try{
				getConnection();
				BasicDBObject doc = new BasicDBObject("title", "myReviews").
					append("service_id", r.service_id).
					append("user_id", r.user_id).
					append("user_name", r.user_name).
					append("user_age", r.user_age).
					append("user_gender", r.user_gender).
					append("review_rating", r.review_rating).
					append("review_date", r.review_date).
					append("review_text", r.review_text);
				myReviews.insert(doc);
				return true;
			}catch(Exception e){
			     return false;
			}
	}

	public static HashMap<Integer, ArrayList<Review>> getReviews(){
		try{
      getConnection();
			DBCursor cursor = myReviews.find();
			HashMap<Integer, ArrayList<Review>> reviews = new HashMap<Integer, ArrayList<Review>>();

			while (cursor.hasNext()){
				BasicDBObject obj = (BasicDBObject) cursor.next();
				ArrayList<Review> list = reviews.getOrDefault(obj.getInt("service_id") , new ArrayList<Review>());
				Review r = new Review(obj.getInt("service_id"), obj.getString("user_id"), obj.getString("user_name"), obj.getInt("user_age"), obj.getString("user_gender"), obj.getDouble("review_rating"), obj.getString("review_date"), obj.getString("review_text"));
				list.add(r);
				reviews.put(obj.getInt("service_id"), list);
			}

			return reviews;
		}catch(Exception e){
			return null;
		}
	}

	public static  ArrayList<BestRated> topBestRatedProducts(){
		try{
			ArrayList<BestRated> best_rated = new ArrayList<BestRated>();
			HashMap<Integer, ArrayList<Review>> reviews = getReviews();
			HashMap<Integer, Double> unsorted = new HashMap();
			LinkedHashMap<Integer, Double> sorted = new LinkedHashMap();

			ArrayList<BestRated> br = new ArrayList();

			for(Map.Entry<Integer, ArrayList<Review>> entry :reviews.entrySet()){

				int sid = entry.getKey();
				ArrayList<Review> list = entry.getValue();

				double avg = 0.0;

				for(Review r : list){
					avg += r.review_rating;
				}
				avg /= (list.size());

				unsorted.put(sid, avg);
			}

			unsorted.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

			int limit=5;

			for(Map.Entry<Integer, Double> entry : sorted.entrySet()){
				if(limit==0)
					break;
				best_rated.add(new BestRated(entry.getKey(), entry.getValue()));
				limit--;
			}

			return best_rated;
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

}
