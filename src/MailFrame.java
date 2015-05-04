import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;


public class MailFrame extends JLabel {

	private static final long serialVersionUID = 1L;
	private static JTextField topicField;
	private static JTextField toField;
	private static JTextArea text;
	static List sentList = new List();
	static List inboxList = new List();
	static JTextPane currentSentMailPane = new JTextPane();
	static JTextPane currentInboxMailPane = new JTextPane();
	private static Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
	private int DEVIDER = 170;
	static Mail currentMail;
	
	public MailFrame() {

		setLayout(new GridLayout());
		
		//SPLITPANES
		JSplitPane inbox = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inboxList, currentInboxMailPane);
		inbox.setOneTouchExpandable(true);
		inbox.setDividerLocation(DEVIDER);

		JSplitPane sent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentList, currentSentMailPane);
		sent.setOneTouchExpandable(true);
		sent.setDividerLocation(DEVIDER);

		//NEW MAIL TAB
		JLabel newMail = new JLabel();
		newMail.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 1, 1, 1);

		try {
			newMail.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("newMailBlue.png"))));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//NEW MAIL LABELS
		JLabel topic = new JLabel("Topic: ");
		topic.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 1;
		newMail.add(topic, gbc);

		JLabel to = new JLabel("    To: ");
		to.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 0;
		newMail.add(to, gbc);

		//TEXT FIELDS
		topicField = new JTextField(50);
		topicField.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 1;
		newMail.add(topicField, gbc);

		toField = new JTextField(50);
		toField.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 0;
		newMail.add(toField, gbc);

		//TEXT AREA
		text = new JTextArea(23, 62);
		text.setLineWrap(true);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		JScrollPane textScroll = new JScrollPane(text);
		newMail.add(textScroll, gbc);

		//SEND MAIL BUTTON
		JButton sendButton = new JButton("Send");
		sendButton.setPreferredSize(new Dimension(60, 30));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		newMail.add(sendButton, gbc);

		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (toField.getText().length() == 0) {
					Client.popupWindow("Enter recipient");
				} else if (topicField.getText().length() == 0) {
					Client.popupWindow("Enter topic");
				} else if (text.getText().length() == 0) {
					Client.popupWindow("Cannot send empty mail");
				} else if (text.getText().length() > Integer.MAX_VALUE ) {
					Client.popupWindow("Mail too long");
				} else {
					try {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String date = dateFormat.format(new Date());
						Client.send("sendMail\n" + toField.getText() + "\n" + Client.userName + 
								"\n" + topicField.getText() + "\n" + text.getText() + "\n" + date);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		//TABS
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Sent", sent);
		tabs.addTab("Inbox", inbox);
		tabs.addTab("New mail", newMail);
		add(tabs);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	public static void clearMailTab () {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = dateFormat.format(new Date());
		currentMail = new Mail(toField.getText(), Client.userName, topicField.getText(), text.getText(), date);
		sentList.addMail(currentMail, currentSentMailPane, false);
		topicField.setText(null);
		toField.setText(null);
		text.setText(null);
	}
}
