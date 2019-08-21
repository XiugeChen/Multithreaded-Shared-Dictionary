package clientPresentation;

import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class HomePagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String BACKGROUND_PATH = "resources/background.png";
	
	private Image background = null;

	public HomePagePanel() {
		createButtons();
		try {
			this.background = new ImageIcon(BACKGROUND_PATH).getImage();
			setSizes(this.background);
		} catch (Exception e) {
			System.out.println("[INFO]: Load background in home page failed");
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (background != null)
			g.drawImage(background, 0, 0, null);
	}
	
	private void createButtons() {
		// initialize buttons
		JButton queryButton = new JButton("Query Word");
		JButton addButton = new JButton("Add Word");
		JButton removeButton = new JButton("Remove Word");
		JButton quitButton = new JButton("Quit");
		
		queryButton.setToolTipText("Query the meaning of a word");
		addButton.setToolTipText("Add word and its meaning");
		removeButton.setToolTipText("Remove word and its meaning");
		quitButton.setToolTipText("Quit the program");
				
		// set button functions
		queryButton.addActionListener((event) -> ClientGUIControl.getInstance().queryWord());
		quitButton.addActionListener((event) -> System.exit(0));

		createLayout(queryButton, addButton, removeButton, quitButton);
	}
	
	private void createLayout(JComponent... arg) {
        GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);
        
        //creates gaps between components and the edges of the container
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        // set group rules
        ParallelGroup pg = gl.createParallelGroup();
        SequentialGroup sg = gl.createSequentialGroup()
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        
        for (JComponent item: arg) {
        	pg.addComponent(item);
        	sg.addComponent(item);
        }
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pg)
        );

        gl.setVerticalGroup(sg);

        gl.linkSize(SwingConstants.HORIZONTAL, arg);
    }
	
	private void setSizes(Image img) {
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	}
}
