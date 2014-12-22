package swdev.rfidreader;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
	public static String rfid = null;
	public static RFIDListener AppRFID = null;
	public static JSONArray array;
	public static Login form;
	private boolean late ;
	public static void main(String[] args) {
		getAllData();
		form = new Login();
		form.setVisible(true);

		AppRFID = new RFIDListener();

		(new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				AppRFID.start();
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rfid = AppRFID.getRfid();
					if (rfid != null) {
						try {
							Thread.sleep(1000);
							App.CheckRFID();
							AppRFID.setRfid(null);
							Thread.sleep(1000);
						} catch (Exception e) {
							System.out.println("Connection Fail");
							e.printStackTrace();
						}
						AppRFID = new RFIDListener();
						AppRFID.start();
					}

				}
			}
		})).start();
	}

	public static void getAllData() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://localhost:8080/api/book");
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			array = new JSONArray(sb.toString());

		} catch (Exception e) {
		}

	}

	public static void CheckRFID() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost requestpost = new HttpPost("http://localhost:8080/api/log");
			for (int i = 0; i < array.length(); i++) {
				JSONObject person = array.getJSONObject(i);
				if (person.getString("RFID").equals(rfid)) {
					StringEntity params = new StringEntity(person.toString());
					requestpost.setHeader("Content-type", "application/json");
					requestpost.setEntity(params);
					HttpResponse response = client.execute(requestpost);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					JSONArray json = new JSONArray('[' + sb.toString() + ']');

					form.setName(json.getJSONObject(0).getString("Name"));
					form.setDate(Integer.toString(json.getJSONObject(0).getInt("Hour"))
							+ ":" + Integer.toString(json.getJSONObject(0).getInt("Min"))
							+ ":" + Integer.toString(json.getJSONObject(0).getInt("Sec")));
					if(json.getJSONObject(0).getInt("Hour") > 8)
						person.put("late", person.getInt("late")+1);
					updateStat(person);
						
					// System.out.println(sb.toString());
					System.out.println(person.getString("First_Name"));
					break;
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public static void updateStat(JSONObject p){
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost requestpost = new HttpPost("http://localhost:8080/api/stat");
		try {
			StringEntity params = new StringEntity(p.toString());
			requestpost.setHeader("Content-type", "application/json");
			requestpost.setEntity(params);
			HttpResponse response = client.execute(requestpost);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
