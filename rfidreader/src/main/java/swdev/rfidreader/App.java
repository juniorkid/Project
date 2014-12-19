package swdev.rfidreader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		String rfid = "000000000";
		String temp;
		int c = 0;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://localhost:8080/api/book");
			HttpPost requestpost= new HttpPost("http://localhost:8080/api/log");
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONArray array = new JSONArray(sb.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject book = array.getJSONObject(i);
				if(book.getString("RFID").equals(rfid)){
					StringEntity params = new StringEntity(book.toString());
					requestpost.setHeader("Content-type", "application/json");
					requestpost.setEntity(params);
					System.out.println(book.getString("First_Name"));
					HttpResponse responepost = client.execute(requestpost);
 				   
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
