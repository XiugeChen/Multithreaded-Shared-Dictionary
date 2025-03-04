package clientPresentation;

import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import clientApplication.ClientAppFacade;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.TRAILING;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class QueryPagePanel extends ClientPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int RETURN_WIDTH = 500;
	private static final int RETURN_HEIGHT = 200;
	private static final int INPUT_WIDTH = 100;
	
	@SuppressWarnings("rawtypes")
	private JList returnList;
    @SuppressWarnings("rawtypes")
	private DefaultListModel model;
    private JTextField wordText;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public QueryPagePanel() {
		super();
	
		model = new DefaultListModel();
		model.addElement("(Query result will be displayed here)");
		returnList = new JList(model);
		wordText = new JTextField(INPUT_WIDTH);
		
		returnList.setCellRenderer(new MyCellRenderer());
		createButtons();
	}
	
	private void createButtons() {
		// initialize buttons
		JButton queryButton = new JButton("Query");
		JButton backButton = new JButton("Back");
		JButton quitButton = new JButton("Quit");
		
		queryButton.setToolTipText("Query the meaning of a word");
		backButton.setToolTipText("Back to home page");
		quitButton.setToolTipText("Quit the program");
				
		// set button functions
		queryButton.addActionListener(new QueryAction());
		backButton.addActionListener((event) -> ClientGUIControl.getInstance().backToHome());
		quitButton.addActionListener((event) -> ClientAppFacade.getInstance().exit());
		
		returnList.setBorder(BorderFactory.createEtchedBorder());
		
		createLayout(returnList, queryButton, backButton, quitButton);
	}
	
	@SuppressWarnings("rawtypes")
	private void createLayout(JList returnList, JComponent... arg) {
        GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);
        
        JLabel wordLabel = new JLabel("Input Word:");
        
        //creates gaps between components and the edges of the container
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        // set group rules
        ParallelGroup pg = gl.createParallelGroup();
        SequentialGroup sg = gl.createSequentialGroup()
        		.addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(wordLabel)
                        .addComponent(wordText))
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
                        .addComponent(wordLabel))
                .addGroup(gl.createParallelGroup()
                        .addComponent(wordText)
                        .addComponent(returnList, RETURN_WIDTH, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE))
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pg)
        );

        gl.setVerticalGroup(sg);

        gl.linkSize(SwingConstants.HORIZONTAL, arg);
    }
	
	private class QueryAction extends AbstractAction{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
	    public void actionPerformed(ActionEvent e) {
            if (!model.isEmpty())
                model.clear();
            
            String result = ClientAppFacade.getInstance().query(wordText.getText());
            
            model.addElement(result);
		}
	}
	
	@SuppressWarnings("serial")
	public class MyCellRenderer extends DefaultListCellRenderer{
        private final JPanel p = new JPanel(new BorderLayout());
        private final JLabel lt = new JLabel();
        String pre = "<html><body style='width: 400px;'>";

        public MyCellRenderer() {
            p.add(lt, BorderLayout.CENTER);
        }

        @SuppressWarnings("rawtypes")
		@Override
        public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean hasFocus){
            final String text = (String) value;
            lt.setText(pre + text);

            return p;
        }
    }
}
