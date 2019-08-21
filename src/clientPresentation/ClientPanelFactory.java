package clientPresentation;

import javax.swing.JPanel;

public class ClientPanelFactory {
	private static ClientPanelFactory instance = null;
	private JPanel homePagePanel;
	
	private ClientPanelFactory() {
		homePagePanel = new HomePagePanel();
	}
	
	public static ClientPanelFactory getInstance() {
		if (instance == null)
			instance = new ClientPanelFactory();
		
		return instance;
	}
	
	// getter and setter
	public JPanel getHomePagePanel() {
		return this.homePagePanel;
	}
}
