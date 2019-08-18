
import java.awt.EventQueue;

import clientPresentation.ClientStartPage;

public class runClient {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			ClientStartPage gui = new ClientStartPage();
			gui.setVisible(true);
        });
	}
}
