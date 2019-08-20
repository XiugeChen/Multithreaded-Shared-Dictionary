package clientPresentation;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class ClientStartPage extends JFrame {
	public ClientStartPage() {
		initUI();
	}
	
	private void initUI() {
		createMenuBar();
		createButtons();
        
        // set image icon
        ImageIcon dicIcon = new ImageIcon("resources/dictionaryIcon.png");
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
	
	private void createButtons() {
		// initialize buttons
		JButton quitButton = new JButton("Quit");
		quitButton.setToolTipText("Quit the program");
				
		// set button functions
		quitButton.addActionListener((event) -> System.exit(0));
				
		// add keyboard short cut
		quitButton.setMnemonic(KeyEvent.VK_B);

		createLayout(quitButton);
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		// all icon image file
		ImageIcon exitIcon = new ImageIcon("resources/exitIcon.png");
		ImageIcon addIcon = new ImageIcon("resources/addIcon.png");
		ImageIcon queryIcon = new ImageIcon("resources/queryIcon.png");
		ImageIcon removeIcon = new ImageIcon("resources/removeIcon.png");
		
		// create file menu
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        // create new window submenu in file menu
        JMenu newMenu = new JMenu("New ^N");
        newMenu.setMnemonic(KeyEvent.VK_N);
        
        // items in new submenu, query, add, remove
        JMenuItem queryMenuItem = new JMenuItem("Query", queryIcon);
        queryMenuItem.setMnemonic(KeyEvent.VK_Q);
        queryMenuItem.setToolTipText("Query word");
        queryMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK));
        
        JMenuItem addMenuItem = new JMenuItem("Add", addIcon);
        addMenuItem.setMnemonic(KeyEvent.VK_A);
        addMenuItem.setToolTipText("Add new word");
        addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.CTRL_DOWN_MASK));
        
        JMenuItem removeMenuItem = new JMenuItem("Remove", removeIcon);
        removeMenuItem.setMnemonic(KeyEvent.VK_R);
        removeMenuItem.setToolTipText("Remove existing word");
        removeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                InputEvent.CTRL_DOWN_MASK));
        
        newMenu.add(queryMenuItem);
        newMenu.add(addMenuItem);
        newMenu.add(removeMenuItem);
        
        fileMenu.add(newMenu);
        fileMenu.addSeparator();
        
        // create exit item in file menu
        JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                InputEvent.CTRL_DOWN_MASK));
        
        exitMenuItem.addActionListener((event) -> System.exit(0));
        
        // create help menu 
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem subjectMenuItem = new JMenuItem("Website");
        subjectMenuItem.setMnemonic(KeyEvent.VK_W);
        subjectMenuItem.setToolTipText("Go to subject website");
        subjectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        
        helpMenu.add(subjectMenuItem);

        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}


