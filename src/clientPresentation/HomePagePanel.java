package clientPresentation;

import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import clientApplication.ClientAppFacade;

public class HomePagePanel extends ClientPanel {
	public HomePagePanel() {
		super();
		createButtons();
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
		addButton.addActionListener((event) -> ClientGUIControl.getInstance().addWord());
		removeButton.addActionListener((event) -> ClientGUIControl.getInstance().removeWord());
		quitButton.addActionListener((event) -> ClientAppFacade.getInstance().exit());

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
}
