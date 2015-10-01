package InterfaceMiddleware;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FenTopic extends JFrame{

	public FenTopic(String pseudo){
		setTitle("Fenetre des topics");
		setSize(400, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		panel.add(new JLabel("Vous êtes "+pseudo));
		
		add(panel);
	    setVisible(true);
	}
}
