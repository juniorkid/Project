package swdev.rfidreader;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
public class Login extends JFrame{
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable (){
			public void run() {
				Login form = new Login();
				form.setVisible(true);
			}
		});
		
	}
	
	public Login(){
		
		// Create Form Frame
		super("Login Form");
		setSize(450,300);
		setLocation(500, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
			
		// Label Result
		final JLabel lblResult = new JLabel("Result", JLabel.CENTER);
		lblResult.setBounds(26, 54, 370, 14);
		getContentPane().add(lblResult);
		
		//Create Button Open
		JButton btnButton = new JButton("Open Message Box");
		btnButton.setBounds(128, 93, 162, 23);
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				String s = (String) JOptionPane.showInputDialog(null,
				"Hey !\n" + "\"Input your name?\"",
				"Title Dialog", JOptionPane.PLAIN_MESSAGE, null,
				null, "Name");
				
				//If a string was returned , say so.
				if((s != null) && (s.length()>0)){
					lblResult.setText("Hello..." + s + "!");
				}
			}
		});
		getContentPane().add(btnButton);
	}

	
	public void loginCheck() {
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
				if (account.getString("Login").equals(user) && account.getString("Password").equals(pass)) {
					System.out.println(account.getString("RFID"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
