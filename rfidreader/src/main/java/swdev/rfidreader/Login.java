package swdev.rfidreader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class Login {
	public static void main(String[] args) {
		String user = "DREAM123";
		String pass = "aaaaaaaaa";

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://localhost:8080/api/login");
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONArray array = new JSONArray(sb.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject account = array.getJSONObject(i);
				if(account.getString("Login").equals(user) && 
						account.getString("Password").equals(pass)){
					System.out.println(account.getString("RFID"));			   
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
