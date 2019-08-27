package serverPresentation;

import java.awt.Desktop;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import serverApplication.ServerAppFacade;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ServerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	
	private static final String HOME_WEBPAGE = "http://www.cloudbus.org/652/LectureSlides.html";
	
	private static final String HOME_ICON_PATH = "resources/images/homeIcon.png";
	private static final String EXIT_ICON_PATH = "resources/images/exitIcon.png";
	
	private final static Logger logger = Logger.getLogger(ServerFrame.class);
	
	public ServerFrame() {
		initUI();
	}
	
	private void initUI() {
		createMenuBar();
		
		// load start home page
		getContentPane().add(ServerPanelFactory.getInstance().getHomePage());
        
        // window settings
		setTitle("Unimelb COMP90015 Dictionary - Server");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		JMenuItem exitMenuItem = null;
		JMenuItem subjectMenuItem = null;
		
        try {
    		exitMenuItem = new JMenuItem("Exit", new ImageIcon(EXIT_ICON_PATH));
    		subjectMenuItem = new JMenuItem("Website", new ImageIcon(HOME_ICON_PATH));
        } catch (Exception e) {
        	logger.warn(e.toString());
        	logger.warn("Load menu item images failed");
        } finally {
        	if (exitMenuItem == null)
        		exitMenuItem = new JMenuItem("Exit");
        	if (subjectMenuItem == null)
        		subjectMenuItem = new JMenuItem("Website");
        }
        
        // add description(toolTip text), shortcuts  
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                InputEvent.CTRL_DOWN_MASK));
        
        subjectMenuItem.setMnemonic(KeyEvent.VK_W);
        subjectMenuItem.setToolTipText("Go to subject website");
        subjectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        
        // add functions to menu items
        exitMenuItem.addActionListener((event) -> ServerAppFacade.getInstance().exit());
        subjectMenuItem.addActionListener((event) -> openWebpage(HOME_WEBPAGE));
        
        // add items to menu
        fileMenu.add(exitMenuItem);
        helpMenu.add(subjectMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
	
	private static void openWebpage(String urlString) {
	    try {
	        Desktop.getDesktop().browse(new URL(urlString).toURI());
	    } catch (Exception e) {
	    	logger.error(e.toString());
	        logger.error("Open webpage \"" + urlString + "\" failed");
	    }
	}
}
