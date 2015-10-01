package InterfaceMiddleware;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FenConnection extends JFrame{
	
	private JTextField jtf;
	
	public FenConnection(){
		setTitle("Identification");
		setSize(400, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
	    jtf = new JTextField("");
	    jtf.setFont(new Font("Arial", Font.BOLD, 14));
	    jtf.setPreferredSize(new Dimension(150, 30));
	    jtf.setForeground(Color.GRAY);
	    jtf.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
    			openFenTopic();
	        };
	    });
	    
	    panel.add(new JLabel("Votre pseudo : "));
	    panel.add(jtf);
	    
	    JButton bouton = new JButton("Valider");
		bouton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openFenTopic();
				
			}
		});
		panel.add(bouton);
	    
	    add(panel);
	    setVisible(true);
	}
	
	public void openFenTopic(){
		System.out.println(jtf.getText()+" a été identifié");
		FenTopic ft = new FenTopic(jtf.getText());
		dispose();
	}
	
}
