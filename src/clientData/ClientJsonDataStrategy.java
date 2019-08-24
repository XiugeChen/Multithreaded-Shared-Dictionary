package clientData;

import org.json.simple.JSONObject;

import clientApplication.ClientApplication;

public class ClientJsonDataStrategy implements ClientDataStrategy {

	@Override
	public String packRequest(String command, String word, String meaning) {
		if (command == null || word == null || 
				command.isEmpty() || word.isEmpty())
			return ClientApplication.EXIT_COMMAND;
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("command", command);
			json.put("word", word);
			
			switch (command.toLowerCase()) {
				case "add":
					if (meaning == null || meaning.isEmpty()) {
						json.put("isValid", "false");
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
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("[ERROR]: JSON data create failed for: " + command 
					+ " - " + word);
			return ClientApplication.EXIT_COMMAND;
		}
		
		return json.toString();
	}

	@Override
	public String resolveRespond(String data) {
		if (data == null || data.isEmpty())
			return "";
		
		return data;
	}
	
}
