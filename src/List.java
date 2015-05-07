import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;


public class List extends JLabel {

	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	
	public List () {
		setLayout(new GridLayout());
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);
		
	}
	
	public void addMail (final Mail mail, final JTextPane currentMailPane, Boolean inbox) {
		JButton button = null;
		if (inbox) {
			button = new JButton(mail.from + "  " + mail.date);
		} else {
			button = new JButton(mail.to + "  " + mail.date);
		}
		panel.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentMailPane.setFont(new Font("Verdana", Font.PLAIN, 14));
				currentMailPane.setText(mail.topic + "\n\n" + mail.text);
			}
		});
	}
}
