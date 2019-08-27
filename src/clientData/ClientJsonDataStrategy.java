package clientData;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import clientApplication.ClientApplication;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ClientJsonDataStrategy implements ClientDataStrategy {
	private final static Logger logger = Logger.getLogger(ClientDataStrategy.class);

	@SuppressWarnings("unchecked")
	@Override
	public String packRequest(String command, String word, String meaning) {
		if (command == null || word == null || 
				command.isEmpty() || word.isEmpty()) {
			logger.error("User request null or invalid (empty command or word)");
			return ClientApplication.EXIT_COMMAND;
		}
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("command", command.toLowerCase());
			json.put("word", word.toLowerCase());
			
			switch (command.toLowerCase()) {
				case "add":
					if (meaning == null || meaning.isEmpty()) {
						json.put("isValid", "false");
						logger.error("User add new word: " + word + ", with empty meaning");
					}
					else {
						json.put("meaning", meaning);
						json.put("isValid", "true");
					}
					break;
				case "query":
				case "remove":
					json.put("isValid", "true");
					break;
				default:
					json.put("isValid", "false");
					logger.info("User send request with unkown command: " + command);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error("User side JSON request create failed for: " + command 
					+ " - " + word);
			return ClientApplication.EXIT_COMMAND;
		}
		
		logger.info("Pack user request successfully");
		return json.toString();
	}

	@Override
	public String resolveRespond(String data) {
		if (data == null || data.isEmpty()) {
			logger.error("Recieve empty or null response from server");
			return "";
		}
		
		logger.info("Resolve server response successfully");
		return data;
	}
	
}
