import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client {

	private static Socket socket;
	private static JFrame frame;
	static String userName;

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

	public static void send(String msg) throws UnknownHostException, IOException {

		socket = new Socket("adam", 9090);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(msg);

		InputStreamReader reader = new InputStreamReader(socket.getInputStream());
		BufferedReader input = new BufferedReader(reader);

		parseInput(input);

		socket.close();
	}

	private static void parseInput(BufferedReader input) {

		try {
			String i = input.readLine();
			switch (i) {
			case "success": 
				userName = input.readLine();
				mailPanel();
				getMailboxes(input);
				break;
			case "wrongPwd":
				popupWindow("Wrong password");
				break;
			case "notExist":
				popupWindow("User does not exist");
				break;
			case "mailSent":
				popupWindow("Mail sent");
				MailFrame.clearMailTab();
				break;
			}
		} catch (IOException e) {
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
}