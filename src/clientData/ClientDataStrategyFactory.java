package clientData;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ClientDataStrategyFactory {
	private static ClientDataStrategyFactory instance = null;
	private ClientJsonDataStrategy jsonStrategy;
	
	private ClientDataStrategyFactory() {
		jsonStrategy = new ClientJsonDataStrategy();
	}
	
	public static ClientDataStrategyFactory getInstance() {
		if (instance == null)
			instance = new ClientDataStrategyFactory();
		
		return instance;
	}
	
	public ClientDataStrategy getJsonStrategy() {
		return jsonStrategy;
	}
}
