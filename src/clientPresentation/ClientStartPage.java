package clientPresentation;

import java.awt.Container;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientStartPage extends JFrame {
	public ClientStartPage() {
		initUI();
	}
	
	private void initUI() {
		// initialize buttons
		JButton quitButton = new JButton("Quit");
		quitButton.setToolTipText("Quit the program");
		
		// set button functions
		quitButton.addActionListener((event) -> System.exit(0));
		
		// add keyboard short cut
		quitButton.setMnemonic(KeyEvent.VK_B);

        createLayout(quitButton);
        
        // set image icon
        ImageIcon dicIcon = new ImageIcon("resources/dictionaryImage.jpeg");
        setIconImage(dicIcon.getImage());
		
        // window settings
		setTitle("Magic Dictionary");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createLayout(JComponent... arg) {
		
		JPanel pane = (JPanel) getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);
        
        pane.setToolTipText("Content pane");
        
        //creates gaps between components and the edges of the container
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );
    }
}
