package serverPresentation;

import javax.swing.JPanel;

public class ServerPanelFactory {
	private static ServerPanelFactory instance = null;
	private ServerHome home = null;
	
	private ServerPanelFactory() {
		home = new ServerHome();
	}
	
	public static ServerPanelFactory getInstance() {
		if (instance == null)
			instance = new ServerPanelFactory();
		
		return instance;
	}
	
	public ServerHome getHomePage() {
		return home;
	}
}
