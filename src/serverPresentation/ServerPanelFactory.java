package serverPresentation;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
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
