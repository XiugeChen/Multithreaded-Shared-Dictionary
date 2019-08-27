package serverApplication;

import serverPresentation.ServerGUIControl;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ServerAppFacade {
	private static ServerAppFacade instance = null;
	private ServerApplication serverApp;
	
	private ServerAppFacade() {
		serverApp = new ServerApplication();
	}
	
	public static ServerAppFacade getInstance() {
		if (instance == null)
			instance = new ServerAppFacade();
		
		return instance;
	}
	
	public void runServer() {
		ServerGUIControl.getInstance().runServerGUI();
		serverApp.run();
		System.exit(1);
	}
	
	public void exit() {
		serverApp.exit();
		System.exit(1);
	}
	
	public boolean rcvCmdArgs(String[] args) {
		if (serverApp.setCmd(args))
			return true;
		
		return false;
	}
}
