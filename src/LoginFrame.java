import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;


@SuppressWarnings("serial")
public class LoginFrame extends JLabel {
	
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
		
		final JTextField userName = new JTextField(20);
		userName.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(userName, gbc);
		
		final JPasswordField password = new JPasswordField(20);
		password.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(password, gbc);
		
		
		//BUTTON
		JButton loginButton = new JButton("Login");
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(loginButton, gbc);
		loginButton.setPreferredSize(new Dimension(70, 30));
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userName.getText().length() == 0) {
					Client.popupWindow("Enter user name");
				} else if (password.getText().length() == 0) {
					Client.popupWindow("Enter password");
				} else {
					try {
						Client.send("login\n" + userName.getText() + "\n" + password.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
			
		});
	}
}
