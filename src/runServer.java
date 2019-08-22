import serverApplication.ServerAppFacade;

public class runServer {
	public static void main(String[] args) {
		if (ServerAppFacade.getInstance().rcvCmdArgs(args))
			ServerAppFacade.getInstance().runServer();
	}
}
