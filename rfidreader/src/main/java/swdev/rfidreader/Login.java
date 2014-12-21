package swdev.rfidreader;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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
public class Login extends JFrame {
	private final JLabel lblName;
	private final JTextField tfName;
	private final JLabel lblTime;
	private final JTextField tfTime;
	public Login() {
		// Create Form Frame
		super("Login Form");
		setSize(450, 300);
		setLocation(500, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		// Label Result
		lblName = new JLabel("Name : ");
		tfName = new JTextField();
		tfName.setEnabled(false);
		lblTime = new JLabel("Time : ");
		tfTime = new JTextField();
		tfTime.setEnabled(false);
		lblName.setBounds(26, 54, 370, 14);
		tfName.setBounds(80, 54, 200, 18);
		lblTime.setBounds(26, 80, 370, 14);
		tfTime.setBounds(80, 80, 200, 18);
		getContentPane().add(lblName);
		getContentPane().add(lblTime);
		getContentPane().add(tfName);
		getContentPane().add(tfTime);

		// Create Button Open
		JButton btnButton = new JButton("Forgot RFID card");
		btnButton.setBounds(240, 220, 162, 23);
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// ///////////////////// *********** Test Login*************
				// ///////////////////////////

				JTextField username = new JTextField();
				JTextField password = new JPasswordField();
				Object[] message = { "Username:", username, "Password:", password};

				int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					loginCheck(username.getText(),password.getText());
				} else {
					System.out.println("Login canceled");
				}

			}
			// ///////////////////// *********** Test Login*************
			// ///////////////////////////
		});
		getContentPane().add(btnButton);
	}
	
	public void setName(String name){
		tfName.setText(name);
	}
	
	public void setDate(String date){
		tfTime.setText(date);
	}
	
	public void loginCheck(String user, String pass) {
		System.out.println(user+"  "+pass);
		try {
			
			//MD5
			byte[] bytePassword = pass.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md5pass = md5.digest(bytePassword);
			String md5StringPassword = new String(md5pass);
			
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
		/*		if (account.getString("Login").equals(user) && account.getString("Password").equals(pass)) {
					System.out.println(account.getString("RFID"));
				}
				else
					System.out.println("FAIL");*/
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
