package clientApplication;

import org.apache.log4j.Logger;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ClientAppFacade {
	private final static Logger logger = Logger.getLogger(ClientAppFacade.class);
	
	private static ClientAppFacade instance;
	private ClientApplication clientApp;
	
	private ClientAppFacade() {
		clientApp = new ClientApplication();
	}
	
	public static ClientAppFacade getInstance() {
		if (instance == null)
			instance = new ClientAppFacade();
		
		return instance;
	}
	
	public String query(String word) {
		if (word.isEmpty()) {
			logger.warn("User input empty word while querying word");
			return "[Warning] Query word can not be empty";
		}
		
		return clientApp.queryWord(word);
	}
	
	public String add(String word, String meaning) {
		if (word.isEmpty() || meaning.isEmpty()) {
			logger.warn("User input empty word or meaning while adding new word");
			return "[Warning] New word and its meaning can not be empty";
		}
		
		return clientApp.addWord(word, meaning);
	}
	
	public String remove(String word) {
		if (word.isEmpty()) {
			logger.warn("User input empty word while removing word");
			return "[Warning] Remove word can not be empty";
		}
		
		return clientApp.removeWord(word);
	}
	
	public void exit() {
		clientApp.exit();
		logger.info("User exit client program");
		System.exit(1);
	}
	
	public boolean rcvCmdArgs(String[] args) {
		if (clientApp.setCmd(args)) {
			clientApp.setConnection();
			return true;
		}
		
		return false;
	}
}
