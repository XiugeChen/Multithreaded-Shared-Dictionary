package serverData;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ServerDataStrategyFactory {
	private static ServerDataStrategyFactory instance = null;
	private ServerJsonDataStrategy json = null;
	
	private ServerDataStrategyFactory() {
		json = new ServerJsonDataStrategy();
	}
	
	public static ServerDataStrategyFactory getInstance() {
		if (instance == null)
			instance = new ServerDataStrategyFactory();
		
		return instance;
	}
	
	public ServerDataStrategy getJsonStrategy() {
		return json;
	}
}
