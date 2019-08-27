import clientApplication.ClientAppFacade;
import clientPresentation.ClientGUIControl;

/**
 * Run client application
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class runClient {
	public static void main(String[] args) {
		long ms = System.currentTimeMillis();
		
		System.setProperty("my.log", "resources/log/client-" + ms + ".log");
		
		if (ClientAppFacade.getInstance().rcvCmdArgs(args))
			ClientGUIControl.getInstance().runClientGUI();
	}
}
