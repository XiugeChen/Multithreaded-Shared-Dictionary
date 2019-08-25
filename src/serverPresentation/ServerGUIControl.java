package serverPresentation;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerGUIControl {
	private static ServerGUIControl instance = null;
	private ServerFrame serverFrame;
	
	private ServerGUIControl() {
		serverFrame = new ServerFrame();
	}
	
	public static ServerGUIControl getInstance() {
		if (instance == null)
			instance = new ServerGUIControl();
		
		return instance;
	}
	
	public void runServerGUI() {
		EventQueue.invokeLater(() -> {
			serverFrame.setVisible(true);
        });
	}
	
	public void addAction(String ip, String port, String request, String respond) {
		ServerPanelFactory.getInstance().getHomePage().addAction(ip, port, request, respond);
	}
	
	public void addConnection(String ip, String port) {
		ServerPanelFactory.getInstance().getHomePage().addConnection(ip, port);
	}
	
	public void removeConnection(String ip, String port) {
		ServerPanelFactory.getInstance().getHomePage().removeConnection(ip, port);
	}
}
