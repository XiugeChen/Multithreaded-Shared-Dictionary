import serverApplication.ServerAppFacade;

/**
 * Run server application
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class runServer {
	public static void main(String[] args) {
		System.setProperty("my.log", "resources/log/server.log");
		
		if (ServerAppFacade.getInstance().rcvCmdArgs(args))
			ServerAppFacade.getInstance().runServer();
	}
}
