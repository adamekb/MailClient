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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.codec.binary.Base64;

public class Client {

	private static Socket socket;
	private static JFrame frame;
	private static FileWriter keyWriter;
	private static Key privateKey, publicKey;
	private static FileWriter ipWriter;
	static ArrayList<Contact> contacts = new ArrayList<Contact>();
	static String serverIp;
	static Key serverPublicKey;

	public static void main(String[] args) 
			throws IOException {
		frame = new JFrame("Enigma");
		setIp();
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
			throws UnknownHostException, IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {

		socket = new Socket(serverIp, 9090);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(msg);

		InputStreamReader reader = new InputStreamReader(socket.getInputStream());
		BufferedReader input = new BufferedReader(reader);

		parseInput(input);
		socket.close();
	}

	private static void parseInput(BufferedReader input) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		try {
			String i = input.readLine();

			switch (i) {
			case "serverKey":
				String key = input.readLine();
				serverPublicKey = Rsa.stringToPublicKey(key);
				send("login\n" + Rsa.encrypt(LoginFrame.userName, serverPublicKey) + 
						"\n" + Rsa.encryptPassword(LoginFrame.pwdArray, serverPublicKey));
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
			case "publicKey":
				String name = input.readLine();
				String signature = input.readLine();
				if (validateSignature(name, signature)) {
					String publicKey = input.readLine();
					addNewContact(name, publicKey); 
				}
				break;
			case "aesAdded":
				break;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static boolean validateSignature(String name, String signature) {
		String decryptedSignature = Rsa.decrypt(signature, serverPublicKey);
		return name.equals(decryptedSignature);
	}

	private static void addNewContact(String name, String publicKey) 
			throws NoSuchAlgorithmException, UnknownHostException, InvalidKeySpecException, IOException, InterruptedException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		SecretKeySpec aesKey = (SecretKeySpec) KeyGenerator.getInstance("AES").generateKey();
		String encodedKey = Base64.encodeBase64String(aesKey.getEncoded());

		send("giveAesKey\n" + name + "\n" + LoginFrame.userName + "\n" + Rsa.encrypt(LoginFrame.userName, privateKey) + "\n" + Rsa.encrypt(encodedKey, Rsa.stringToPublicKey(publicKey)) + "\n" + Rsa.encrypt(encodedKey, Client.publicKey));

		contacts.add(new Contact(name, Rsa.stringToPublicKey(publicKey), aesKey));
	}

	private static void addNewContact(String contactInfo) {
		String[] array = contactInfo.split(" ");
		String name = array[0];
		String encryptedAesKey = array[1];
		String stringPublicKey = array[2];

		Key publicKey = Rsa.stringToPublicKey(stringPublicKey);

		String stringAesKey = Rsa.decrypt(encryptedAesKey, privateKey);
		byte[] decodedKey = Base64.decodeBase64(stringAesKey);
		SecretKeySpec aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

		contacts.add(new Contact(name, publicKey, aesKey));
	}

	private static void getMailboxes(BufferedReader input) 
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {

		try {
			String msg = input.readLine();
			while (!(msg.equals("done"))) {
				if (msg.equals("sentMail")) {
					addSentMail(input.readLine(), input.readLine(), input.readLine(), input.readLine(), input.readLine());
				} else if (msg.equals("inboxMail")) {
					addInboxMail(input.readLine(), input.readLine(), input.readLine(), input.readLine(), input.readLine());
				} else if (msg.equals("newContact")) {
					String newContact = input.readLine();
					addNewContact(newContact);
				}
				msg = input.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addInboxMail(String to, String from, String topic,
			String text, String date) throws UnknownHostException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, InterruptedException {
		SecretKeySpec aesKey = getAesKey(from);
		Mail mail = new Mail(to, from, Aes.decrypt(topic, aesKey), Aes.decrypt(text, aesKey), date);
		MailFrame.inboxList.addMail(mail, MailFrame.currentInboxMailPane, true);
	}

	private static void addSentMail(String to, String from, String topic,
			String text, String date) throws UnknownHostException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, InterruptedException {
		SecretKeySpec aesKey = getAesKey(to);
		Mail mail = new Mail(to, from, Aes.decrypt(topic, aesKey), Aes.decrypt(text, aesKey), date);
		MailFrame.sentList.addMail(mail, MailFrame.currentSentMailPane, false);
	}

	public static void popupWindow (String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
	
	public static void changeIp () throws IOException {
		serverIp = JOptionPane.showInputDialog(frame, "Enter ip", "IP configuration", JOptionPane.PLAIN_MESSAGE);
		ipWriter = new FileWriter("ip.txt", true);
		ipWriter.flush();
		ipWriter.write(serverIp);
		ipWriter.close();
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

	public static SecretKeySpec getAesKey(String name) 
			throws UnknownHostException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, InterruptedException {
		SecretKeySpec aesKey = null;
		for (Contact i : contacts) {
			if (i.getUserName().equals(name)) {
				aesKey = i.getAesKey();
			}
		}

		if (aesKey == null) {
			send("addContact\n" + name);
			Thread.sleep(200);

			for (Contact i : contacts) {
				if (i.getUserName().equals(name)) {
					aesKey = i.getAesKey();
				}
			}
		}
		return aesKey;
	}
	
	private static void setIp() throws IOException {
		ipWriter = new FileWriter("ip.txt", true);
		ipWriter.close();
		BufferedReader reader = new BufferedReader(new FileReader("ip.txt"));
		String line = null;
		if ((line = reader.readLine()) != null) {
			serverIp = line;
		}
		reader.close();
	}
}