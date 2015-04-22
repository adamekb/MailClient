import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client {

	private static Socket socket;
	private static ServerSocket listener;
	private static JFrame frame;

	public static void main(String[] args) throws IOException {
		loginPanel();
	}



	public static void loginPanel () {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new JFrame("Enigma");
				frame.add(new MainFrame());
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

	public static void mailPanel () {
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

		socket = new Socket("Adam", 9090);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(msg);

		listener = new ServerSocket(8080);
		socket = listener.accept();
		InputStreamReader reader = new InputStreamReader(socket.getInputStream());
		BufferedReader input = new BufferedReader(reader);

		parseInput(input);

		closeSockets();
	}

	private static void closeSockets() {

		try {
			socket.close();
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void parseInput(BufferedReader input) {

		try {
			String i = input.readLine();
			switch (i) {
			case "success": 
				mailPanel();
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
	
	public static void popupWindow (String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
}