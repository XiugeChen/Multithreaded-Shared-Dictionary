package clientPresentation;

import java.awt.EventQueue;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
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
	
	public void backToHome() {
		clientFrame.display("home");
	}
	
	public void queryWord() {
		clientFrame.display("query");
	}
	
	public void addWord() {
		clientFrame.display("add");
	}
	
	public void removeWord() {
		clientFrame.display("remove");
	}
}
