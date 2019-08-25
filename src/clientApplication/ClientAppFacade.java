package clientApplication;

public class ClientAppFacade {
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
		if (word.isEmpty())
			return "[Warning] Query word can not be empty";
		
		return clientApp.queryWord(word);
	}
	
	public String add(String word, String meaning) {
		if (word.isEmpty() || meaning.isEmpty())
			return "[Warning] New word and its meaning can not be empty";
		
		return clientApp.addWord(word, meaning);
	}
	
	public String remove(String word) {
		if (word.isEmpty())
			return "[Warning] Remove word can not be empty";
		
		return clientApp.removeWord(word);
	}
	
	public void exit() {
		clientApp.exit();
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
