import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;


@SuppressWarnings("serial")
public class MailFrame extends JLabel {

	private static JTextField headerField;
	private static JTextField toField;
	private static JTextArea text;
	private static JList<JTextPane> sentList;
	private static Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
	
	public MailFrame() {

		Dimension minimumSize = new Dimension(100, 50);
		setLayout(new GridLayout());

		//CURRENT MAIL
		JTextArea currentInboxMail = new JTextArea();
		currentInboxMail.setText("INBOX");
		currentInboxMail.setMinimumSize(minimumSize);

		JTextArea currentSentMail = new JTextArea();
		currentSentMail.setText("SENT");
		currentSentMail.setMinimumSize(minimumSize);

		//MAILLISTS
		JList<JLabel> inboxList = new JList<JLabel>();
		inboxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inboxList.setLayoutOrientation(JList.VERTICAL_WRAP);
		JScrollPane inboxScroller = new JScrollPane(inboxList);

		sentList = new JList<JTextPane>();
		sentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sentList.setLayoutOrientation(JList.VERTICAL_WRAP);
		sentList.setBorder(border);
		sentList.setVisible(true);
		JScrollPane sentScroller = new JScrollPane(sentList);
		
		//SPLITPANES
		JSplitPane inbox = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inboxList, currentInboxMail);
		inbox.setOneTouchExpandable(true);
		inbox.setDividerLocation(150);
		inbox.setMinimumSize(minimumSize);

		JSplitPane sent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentList, currentSentMail);
		sent.setOneTouchExpandable(true);
		sent.setDividerLocation(150);
		sent.setMinimumSize(minimumSize);

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
		headerField = new JTextField(50);
		headerField.setBorder(border);
		gbc.gridx = 1;
		gbc.gridy = 1;
		newMail.add(headerField, gbc);

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
				} else if (headerField.getText().length() == 0) {
					Client.popupWindow("Enter topic");
				} else if (text.getText().length() == 0) {
					Client.popupWindow("Cannot send empty mail");
				} else if (text.getText().length() > Integer.MAX_VALUE ) {
					Client.popupWindow("Mail too long");
				} else {
					try {
						Client.send("sendMail\n" + toField.getText() + "\n" + headerField.getText() + "\n" + text.getText() + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		//TABS
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Inbox", inbox);
		tabs.addTab("Sent", sent);
		tabs.addTab("New mail", newMail);
		add(tabs);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	public static void clearMailTab () {
		//addToSent();
		headerField.setText(null);
		toField.setText(null);
		text.setText(null);
	}

	private static void addToSent() {
		JTextPane item = new JTextPane();
		item.setText("asdasda");
		sentList.add(item);
	}
}
