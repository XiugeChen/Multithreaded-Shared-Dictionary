package clientPresentation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class ClientPanel extends JPanel {
	private static final String BACKGROUND_PATH = "resources/images/background.png";
	
	private Image background = null;
	
	public ClientPanel() {
		try {
			this.background = new ImageIcon(BACKGROUND_PATH).getImage();
			setSizes(this.background);
		} catch (Exception e) {
			System.err.println("[INFO]: Load background in home page failed");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (background != null)
			g.drawImage(background, 0, 0, null);
	}
	
	private void setSizes(Image img) {
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	}
}
