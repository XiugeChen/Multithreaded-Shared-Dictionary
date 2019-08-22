import clientApplication.ClientAppFacade;
import clientPresentation.ClientGUIControl;

public class runClient {
	public static void main(String[] args) {
		if (ClientAppFacade.getInstance().rcvCmdArgs(args))
			ClientGUIControl.getInstance().runClientGUI();
	}
}
