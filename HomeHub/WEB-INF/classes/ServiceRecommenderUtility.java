import java.io.*;
import java.sql.*;
import java.io.IOException;
import java.util.*;

public class ServiceRecommenderUtility{

	public static HashMap<String,ArrayList<Integer>> readFile(){
		String TOMCAT_HOME = System.getProperty("catalina.home");
    BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ",";
		HashMap<String,ArrayList<Integer>> serviceRecMap = new HashMap();
		try{
      br = new BufferedReader(new FileReader(new File(TOMCAT_HOME+"\\webapps\\HomeHub\\matrixFactorizationBasedRecommendations.csv")));
      while ((line = br.readLine()) != null) {
        String[] service_rec = line.split(cvsSplitBy,2);
        String user_id = service_rec[0];
        String rec_s = service_rec[1];
        ArrayList<Integer> rec = new ArrayList();
        if(rec_s != null){
          rec_s = rec_s.replace("[","");
  				rec_s = rec_s.replace("]","");
  				rec_s = rec_s.replace("\"","");
  				rec_s = rec_s.replace("'","");
  				rec_s = rec_s.replace(" ","");

          for(String rs : rec_s.split(",")){
            rec.add(Integer.parseInt(rs));
          }
        }
				serviceRecMap.put(user_id,rec);
      }
		} catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
		}
		return serviceRecMap;
	}

}
