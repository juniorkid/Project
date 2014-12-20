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

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Login form = new Login();
				form.setVisible(true);
			}
		});

	}

	public Login() {

		// Create Form Frame
		super("Login Form");
		setSize(450, 300);
		setLocation(500, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		// Label Result
		final JLabel lblName = new JLabel("Name : ");
		final JTextField tfName = new JTextField();
		final JLabel lblTime = new JLabel("Time : ");
		final JTextField tfTime = new JTextField();
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
				Object[] message = { "Username:", username, "Password:", password };

				int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (username.getText().equals("h") && password.getText().equals("h")) {
						System.out.println("Login successful");
					} else {
						System.out.println("login failed");
					}
				} else {
					System.out.println("Login canceled");
				}

			}
			// ///////////////////// *********** Test Login*************
			// ///////////////////////////
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
