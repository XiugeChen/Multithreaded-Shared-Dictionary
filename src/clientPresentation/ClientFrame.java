package clientPresentation;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import clientApplication.ClientAppFacade;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ClientFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int WINDOW_WIDTH = 730;
	private static final int WINDOW_HEIGHT = 390;
	
	private static final String DICT_ICON_PATH = "resources/images/dictionaryIcon.png";
	private static final String NEW_ICON_PATH = "resources/images/addIcon.png";
	private static final String HOME_ICON_PATH = "resources/images/homeIcon.png";
	private static final String EXIT_ICON_PATH = "resources/images/exitIcon.png";
	
	private final static Logger logger = Logger.getLogger(ClientFrame.class);

	public ClientFrame() {
		initUI();
	}
	
	public void display(String page) {
		getContentPane().removeAll();
		getContentPane().invalidate();
		
		switch (page.toLowerCase()) {
			case "add":
				getContentPane().add(ClientPanelFactory.getInstance().getAddPagePanel());
				setTitle("Unimelb COMP90015 Dictionary - Client Add");
				break;
			case "remove":
				getContentPane().add(ClientPanelFactory.getInstance().getRemovePagePanel());
				setTitle("Unimelb COMP90015 Dictionary - Client Remove");
				break;
			case "query":
				getContentPane().add(ClientPanelFactory.getInstance().getQueryPagePanel());
				setTitle("Unimelb COMP90015 Dictionary - Client Query");
				break;
			default:
				getContentPane().add(ClientPanelFactory.getInstance().getHomePagePanel());
				setTitle("Unimelb COMP90015 Dictionary - Client Home");
		}
		
		getContentPane().revalidate();
	}
	
	private void initUI() {
		createMenuBar();
		
		// load start home page
		getContentPane().add(ClientPanelFactory.getInstance().getHomePagePanel());
        
        // set frame image icon
        ImageIcon dicIcon = new ImageIcon(DICT_ICON_PATH);
        setIconImage(dicIcon.getImage());
        
        // window settings
		setTitle("Unimelb COMP90015 Dictionary - Client Home");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		// create file menu
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        // create help menu 
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        // create items in file menu, add image if possible
        JMenuItem newMenuItem = null;
		JMenuItem exitMenuItem = null;
		JMenuItem subjectMenuItem = null;
		
        try {
    		newMenuItem = new JMenuItem("New", new ImageIcon(NEW_ICON_PATH));
    		exitMenuItem = new JMenuItem("Exit", new ImageIcon(EXIT_ICON_PATH));
    		subjectMenuItem = new JMenuItem("Website", new ImageIcon(HOME_ICON_PATH));
        } catch (Exception e) {
        	logger.warn(e.toString());
        	logger.warn("Load menu item images failed");
        } finally {
        	if (newMenuItem == null)
        		newMenuItem = new JMenuItem("New");
        	if (exitMenuItem == null)
        		exitMenuItem = new JMenuItem("Exit");
        	if (subjectMenuItem == null)
        		subjectMenuItem = new JMenuItem("Website");
        }
        
        // add description(toolTip text), shortcuts
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setToolTipText("New window");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_DOWN_MASK));
        
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                InputEvent.CTRL_DOWN_MASK));
        
        subjectMenuItem.setMnemonic(KeyEvent.VK_W);
        subjectMenuItem.setToolTipText("Go to subject website");
        subjectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        
        // add functions to menu items
        exitMenuItem.addActionListener((event) -> ClientAppFacade.getInstance().exit());
        
        // add items to menu
        fileMenu.add(newMenuItem);
        fileMenu.add(exitMenuItem);
        helpMenu.add(subjectMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}


