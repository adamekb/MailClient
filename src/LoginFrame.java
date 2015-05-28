import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class LoginFrame extends JLabel {
	
	private static final long serialVersionUID = 1L;
	public static char[] pwdArray;
	public static String userName;
	
	public LoginFrame() {
		
		try {
            setIcon(new ImageIcon(ImageIO.read(getClass().getResource("globe.png"))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 1, 1, 1);
		setLayout(new GridBagLayout());
		
		//LABEL
		JLabel userLabel = new JLabel("User name: ");
		userLabel.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(userLabel, gbc);
		
		JLabel passLabel = new JLabel("Password: ");
		passLabel.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(passLabel, gbc);
		
		//TEXT
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		final JTextField uName = new JTextField(20);
		uName.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(uName, gbc);
		
		final JPasswordField password = new JPasswordField(20);
		password.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(password, gbc);
		
		
		//BUTTONS
		JButton loginButton = new JButton("Login");
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(loginButton, gbc);
		loginButton.setPreferredSize(new Dimension(70, 30));
		
		JButton ipButton = new JButton("IP");
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 2;
//		gbc.ipady = 0;
//		gbc.insets = new Insets(10,0,0,0); 
//		gbc.anchor = GridBagConstraints.PAGE_END;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(ipButton, gbc);
		ipButton.setPreferredSize(new Dimension(50, 30));
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pwdArray = password.getPassword();
				userName = uName.getText();
				if (userName.length() == 0) {
					Client.popupWindow("Enter user name");
				} else if (pwdArray.length == 0) {
					Client.popupWindow("Enter password");
				} else {
					try {
						Client.send("requestServerKey\n");
					} catch (IOException | InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException e) {
						e.printStackTrace();
					}
				}

			}
			
		});
		
		ipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Client.changeIp();
			}
		});
	}
	
	public static void clearPwdArray() {
		Arrays.fill(pwdArray, '\u0000');
	}
}
