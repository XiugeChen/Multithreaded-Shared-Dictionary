package clientPresentation;

import javax.swing.JPanel;

public class ClientPanelFactory {
	private static ClientPanelFactory instance = null;
	
	private ClientPanelFactory() {
	}
	
	public static ClientPanelFactory getInstance() {
		if (instance == null)
			instance = new ClientPanelFactory();
		
		return instance;
	}
	
	// getter and setter
	public JPanel getHomePagePanel() {
		return new HomePagePanel();
	}
	
	public JPanel getQueryPagePanel() {
		return new QueryPagePanel();
	}
	
	public JPanel getAddPagePanel() {
		return new AddPagePanel();
	}
	
	public JPanel getRemovePagePanel() {
		return new RemovePagePanel();
	} 
}
