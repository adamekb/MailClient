import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class List extends JLabel {

	private static final long serialVersionUID = 1L;

	public List () {
		setLayout(new GridLayout());
		JPanel panel = new JPanel();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton one = new JButton("onedssssssss");
		JButton two = new JButton("two");
		JButton a = new JButton("one");
		JButton s = new JButton("two");
		JButton d = new JButton("one");
		JButton f = new JButton("two");
		JButton g = new JButton("one");
		JButton h = new JButton("two");
		JButton j = new JButton("one");
		JButton k = new JButton("two");
		
		one.setPreferredSize(new Dimension(100, 50));
		two.setPreferredSize(new Dimension(100, 50));
		a.setPreferredSize(new Dimension(100, 50));
		s.setPreferredSize(new Dimension(100, 50));
		d.setPreferredSize(new Dimension(100, 50));
		f.setPreferredSize(new Dimension(100, 50));
		g.setPreferredSize(new Dimension(100, 50));
		h.setPreferredSize(new Dimension(100, 50));
		j.setPreferredSize(new Dimension(100, 50));
		k.setPreferredSize(new Dimension(100, 50));
		
		panel.add(one);
		panel.add(two);
		panel.add(a);
		panel.add(s);
		panel.add(d);
		panel.add(f);
		panel.add(g);
		panel.add(h);
		panel.add(j);
		panel.add(k);
		
		JScrollPane scroll = new JScrollPane(panel);
		add(scroll);
		
	}

}
