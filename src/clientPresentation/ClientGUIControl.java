package clientPresentation;

import java.awt.EventQueue;

/**
 * Singleton control interface of GUI
 * @author xiugechen
 *
 */
public class ClientGUIControl {
	
	private static ClientGUIControl instance = null;
	private ClientFrame clientFrame;
	
	private ClientGUIControl() {
		clientFrame = new ClientFrame();
	}
	
	public static ClientGUIControl getInstance() {
		if (instance == null)
			instance = new ClientGUIControl();
		
		return instance;
	}
	
	public void runClientGUI() {
		EventQueue.invokeLater(() -> {
			clientFrame.setVisible(true);
        });
	}
	
	public void queryWord() {
		clientFrame.displayQueryPage();
	}
}