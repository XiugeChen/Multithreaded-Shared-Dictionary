package clientPresentation;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import clientApplication.ClientAppFacade;

public class AddPagePanel extends ClientPanel {
	private static final int RETURN_WIDTH = 500;
	private static final int RETURN_HEIGHT = 50;
	private static final int INPUT_WIDTH = 100;
	
	private JList returnList;
    private DefaultListModel model;
    private JTextField wordText;
    private JTextField meaningText;
    
    public AddPagePanel() {
		super();
		
		model = new DefaultListModel();
		model.addElement("(Add status will be displayed here)");
		returnList = new JList(model);
		wordText = new JTextField(INPUT_WIDTH);
		meaningText = new JTextField(INPUT_WIDTH);
		
		// extend meaningText vertically 4 times
		Dimension d = meaningText.getPreferredSize();
	    d.height = d.height * 4;
	    meaningText.setPreferredSize(d);
		
		createButtons();
	}
    
    private void createButtons() {
		// initialize buttons
		JButton addButton = new JButton("Add");
		JButton backButton = new JButton("Back");
		JButton quitButton = new JButton("Quit");
		
		addButton.setToolTipText("Add a word and its meaning");
		backButton.setToolTipText("Back to home page");
		quitButton.setToolTipText("Quit the program");
				
		// set button functions
		addButton.addActionListener(new AddAction());
		backButton.addActionListener((event) -> ClientGUIControl.getInstance().backToHome());
		quitButton.addActionListener((event) -> System.exit(0));
		
		returnList.setBorder(BorderFactory.createEtchedBorder());
		
		createLayout(returnList, addButton, backButton, quitButton);
	}
	
	private void createLayout(JList returnList, JComponent... arg) {
        GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);
        
        JLabel wordLabel = new JLabel("Input Word:");
        JLabel meaningLabel = new JLabel("Input Meaning:");
        
        //creates gaps between components and the edges of the container
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        // set group rules
        ParallelGroup pg = gl.createParallelGroup();
        SequentialGroup sg = gl.createSequentialGroup()
        		.addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(wordLabel)
                        .addComponent(wordText))
        		.addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(meaningLabel)
                        .addComponent(meaningText))
        		.addComponent(returnList, RETURN_HEIGHT, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE)
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        
        for (JComponent item: arg) {
        	pg.addComponent(item);
        	sg.addComponent(item);
        }
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
        		.addGroup(gl.createParallelGroup(TRAILING)
                        .addComponent(wordLabel)
                        .addComponent(meaningLabel))
                .addGroup(gl.createParallelGroup()
                        .addComponent(wordText)
                        .addComponent(meaningText)
                        .addComponent(returnList, RETURN_WIDTH, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE))
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pg)
        );

        gl.setVerticalGroup(sg);

        gl.linkSize(SwingConstants.HORIZONTAL, arg);
    }
	
	private class AddAction extends AbstractAction{
		@Override
	    public void actionPerformed(ActionEvent e) {
            if (!model.isEmpty())
                model.clear();
            
            String result = ClientAppFacade.getInstance().add(wordText.getText(), meaningText.getText());
            
            model.addElement(result);
		}
	}
}
