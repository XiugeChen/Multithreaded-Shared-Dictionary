package serverApplication;

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
		serverApp.run();
	}
	
	public boolean rcvCmdArgs(String[] args) {
		if (serverApp.setCmd(args))
			return true;
		
		return false;
	}
}
