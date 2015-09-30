package interfaceMiddleware;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.xswingx.PromptSupport;

public class UI extends JFrame{

	private JFrame ident = new JFrame();
	private JTextField jtf = new JTextField("");
	private JLabel label = new JLabel("Choisissez un pseudo : ");
	private JLabel pseudo = new JLabel("");
	
	public void affichage(){
		ident.setTitle("Identification");
		ident.setSize(400, 100);
		ident.setLocationRelativeTo(null);
		ident.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
	    Font police = new Font("Arial", Font.BOLD, 14);
	    
	    
	    jtf.setFont(police);
	    jtf.setPreferredSize(new Dimension(150, 30));
	    jtf.setForeground(Color.GRAY);
	    jtf.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
    			pseudo.setText(jtf.getText());
    			System.out.println("Vous êtes identifié !");
    			//ident.setVisible(false);
    			JFrame fenetre2 = new JFrame();
    		    fenetre2.setTitle("Forum");
    		    fenetre2.setSize(400, 200);
    		    fenetre2.setLocationRelativeTo(null);
    		    //fenetre2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		    JPanel panel = new JPanel();
    		    panel.add(pseudo);
    		    fenetre2.add(panel);
    		    fenetre2.setVisible(true);
	        };
	    });
	    PromptSupport.setPrompt("Pseudo", jtf);
	    
	    panel.add(label);
	    panel.add(jtf);
	    
		JButton bouton = new JButton(new IHM("Valider"));
		//bouton.addActionListener(this);
		panel.add(bouton);
	    
		ident.add(panel);
		ident.setVisible(true);
	}

	public class IHM extends AbstractAction {
		public IHM(String texte){
			super(texte);
		}
	 
		public void actionPerformed(ActionEvent e) {
			pseudo.setText(jtf.getText());
			System.out.println("Vous êtes identifié !");
			//ident.setVisible(false);
			JFrame fenetre2 = new JFrame();
		    fenetre2.setTitle("Forum");
		    fenetre2.setSize(400, 200);
		    fenetre2.setLocationRelativeTo(null);
		    //fenetre2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    JPanel panel = new JPanel();
		    panel.add(pseudo);
		    fenetre2.add(panel);
		    fenetre2.setVisible(true);
		} 
	}
}
