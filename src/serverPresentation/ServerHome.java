package serverPresentation;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ServerHome extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ActionTableMode actionTableMode;
	private ConnectionTableMode connectTableMode;
	
	public ServerHome() {
		actionTableMode = new ActionTableMode();
		connectTableMode = new ConnectionTableMode();
		
		createButtons();
	}
	
	@SuppressWarnings("unused")
	private void createButtons() {
		JButton quitButton = new JButton("Quit");
		
		quitButton.setToolTipText("Quit the program");
		
		quitButton.addActionListener((event) -> System.exit(1));
		
		// set up display table
		JTable actTable = new JTable(actionTableMode);
		JTable connectTable = new JTable(connectTableMode);
		actionTableMode.addTableModelListener(actTable);
		connectTableMode.addTableModelListener(connectTable);
		
		JScrollPane actTScrollPane = new JScrollPane(actTable);
		JScrollPane connectTScrollPane = new JScrollPane(connectTable);
		
		actTable.setFillsViewportHeight(true);
		connectTable.setFillsViewportHeight(true);
		
		// set column width, last two column > middile two column > first column
		TableColumn column = null;
		for (int i = 0; i < actionTableMode.getColumnCount(); i++) {
		    column = actTable.getColumnModel().getColumn(i);
		    if (i > 2) {
		        column.setPreferredWidth(200);
		    } else {
		        column.setPreferredWidth(100);
		    }
		}
		// set column width for connection table
		for (int i = 0; i < connectTableMode.getColumnCount(); i++) {
		    column = connectTable.getColumnModel().getColumn(i);
		    if (i > 1) {
		        column.setPreferredWidth(200);
		    } else {
		    	column.setPreferredWidth(100);
		    }
		}
		
		createLayout(actTable, connectTable, quitButton);
	}
	
	private void createLayout(JTable actionTable, JTable connectTable, JComponent... arg) {
		GroupLayout gl = new GroupLayout(this);
        this.setLayout(gl);
        
        //creates gaps between components and the edges of the container
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        // set group rules
        ParallelGroup pg = gl.createParallelGroup();
        SequentialGroup sg = gl.createSequentialGroup()
        		.addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(actionTable.getTableHeader()))
        		.addComponent(actionTable)
        		.addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(connectTable.getTableHeader()))
        		.addComponent(connectTable)
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        
        for (JComponent item: arg) {
        	pg.addComponent(item);
        	sg.addComponent(item);
        }
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
        		.addGroup(gl.createParallelGroup(TRAILING)
                        .addComponent(actionTable.getTableHeader())
                        .addComponent(actionTable)
                        .addComponent(connectTable.getTableHeader())
                        .addComponent(connectTable))
                .addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pg)
        );

        gl.setVerticalGroup(sg);

        gl.linkSize(SwingConstants.HORIZONTAL, arg);
	}
	
	public void addAction(String ip, String port, String request, String respond) {
		ArrayList<String> info = new ArrayList<>();
		info.add(ip);
		info.add(port);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		info.add(formatter.format(new Date()));
		info.add(request);
		info.add(respond);
		
		actionTableMode.addValue(info);
	}

	public void addConnection(String ip, String port) {
		ArrayList<String> info = new ArrayList<>();
		info.add(ip);
		info.add(port);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		info.add(formatter.format(new Date()));
		
		connectTableMode.addValue(info);
	}
	
	public void removeConnection(String ip, String port) {
		ArrayList<String> info = new ArrayList<>();
		info.add(ip);
		info.add(port);
		info.add("SKIP");
		
		connectTableMode.removeValue(info);
	}
	
	private class ConnectionTableMode extends ServerTableMode {		
		private static final long serialVersionUID = 1L;

		public ConnectionTableMode() {
			super();
			this.getColumnNames().add("Client IP");
			this.getColumnNames().add("Client Port");
			this.getColumnNames().add("Connected Date");
		}
	}
	
	private class ActionTableMode extends ServerTableMode {
		private static final long serialVersionUID = 1L;

		public ActionTableMode() {
			super();
			this.getColumnNames().add("Client IP");
			this.getColumnNames().add("Client Port");
			this.getColumnNames().add("Request Time");
			this.getColumnNames().add("Request Content");
			this.getColumnNames().add("Server Respond");
		}
	}
}
