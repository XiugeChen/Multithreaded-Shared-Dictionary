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
		
		return "Ehco: " + word;
	}
	
	public String add(String word, String meaning) {
		if (word.isEmpty() || meaning.isEmpty())
			return "[Warning] New word and its meaning can not be empty";
		
		return "Word: " + word + "\nMeaning: " + meaning;
	}
	
	public String remove(String word) {
		if (word.isEmpty())
			return "[Warning] Remove word can not be empty";
		
		return "Remove: " + word;
	}
	
	public boolean rcvCmdArgs(String[] args) {
		if (clientApp.setCmd(args))
			return true;
		
		return false;
	}
}
