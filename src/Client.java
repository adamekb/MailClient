import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.KeyPair;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client {

	private static Socket socket;
	private static JFrame frame;
	private static FileWriter keyWriter;
	private static Key privateKey, publicKey;
	static Key receivedKey;

	public static void main(String[] args) throws IOException {
		frame = new JFrame("Enigma");
		loginPanel();
	}

	private static void loginPanel () {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.add(new LoginFrame());
				frame.setVisible(true);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setSize(960, 540);
				frame.setResizable(false);
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void mailPanel () {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.getContentPane().removeAll();
				frame.add(new MailFrame());
				frame.setVisible(true);
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void send(String msg) 
			throws UnknownHostException, IOException, InterruptedException {

		socket = new Socket("adam", 9090);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(msg);
		Thread.sleep(1000);
		InputStreamReader reader = new InputStreamReader(socket.getInputStream());
		BufferedReader input = new BufferedReader(reader);

		parseInput(input);
		socket.close();
	}

	private static void parseInput(BufferedReader input) {

		try {
			String i = input.readLine();
			
			switch (i) {
			case "serverKey":
				String key = input.readLine();
				receivedKey = Rsa.stringToPublicKey(key);
				send("login\n" + Rsa.encrypt(LoginFrame.userName, receivedKey) + 
						"\n" + Rsa.encryptPassword(LoginFrame.pwdArray, receivedKey));
				LoginFrame.clearPwdArray();
				break;
			case "success": 
				String fileName = LoginFrame.userName + "Keys.txt";
				loadKeys(fileName);
				mailPanel();
				getMailboxes(input);
				break;
			case "loginError":
				popupWindow("Wrong login credentials");
				break;
			case "notExist":
				popupWindow("User does not exist");
				break;
			case "mailSent":
				popupWindow("Mail sent");
				MailFrame.clearMailTab();
				break;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void getMailboxes(BufferedReader input) throws IOException {
		
		try {
			String msg = input.readLine();
			while (!(msg.equals("done"))) {
				if (msg.equals("sentMail")) {
					addSentMail(input.readLine(), input.readLine(), input.readLine(), input.readLine(), input.readLine());
				} else if (msg.equals("inboxMail")) {
					addInboxMail(input.readLine(), input.readLine(), input.readLine(), input.readLine(), input.readLine());
				}
				msg = input.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addInboxMail(String to, String from, String topic,
			String text, String date) {
		Mail mail = new Mail(to, from, topic, Aes.Decrypt(text), date);
		MailFrame.inboxList.addMail(mail, MailFrame.currentInboxMailPane, true);
	}

	private static void addSentMail(String to, String from, String topic,
			String text, String date) {
		Mail mail = new Mail(to, from, topic, Aes.Decrypt(text), date);
		MailFrame.sentList.addMail(mail, MailFrame.currentSentMailPane, false);
	}

	public static void popupWindow (String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
	
	private static void loadKeys(String fileName) 
			throws IOException {
		keyWriter = new FileWriter(fileName, true);
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		if ((line = reader.readLine()) != null) {
			privateKey = Rsa.stringToPrivateKey(line);
			publicKey = Rsa.stringToPublicKey(reader.readLine());
		} else {
			createKeys();
		}
		reader.close();
	}

	private static void createKeys() 
			throws IOException {
		KeyPair newKeys = Rsa.generateNewKeys();
		Key privateKey = newKeys.getPrivate();
		Key publicKey = newKeys.getPublic();
		
		keyWriter.write(Rsa.keyToString(privateKey) + "\n" + Rsa.keyToString(publicKey) + "\n");
		keyWriter.close();
	}
}