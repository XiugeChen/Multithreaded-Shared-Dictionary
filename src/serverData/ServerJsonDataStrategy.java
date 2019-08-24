package serverData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerJsonDataStrategy implements ServerDataStrategy {
	private static final String DEFAULT_DIC_PATH = "resources/data/dictionary.json";
	
	private JSONObject dictData = null;
	private String file = null;
	
	public ServerJsonDataStrategy() {
		dictData = new JSONObject();
	}
	
	@Override
	public boolean setDataSource(String source) {
		file = source;
		JSONParser jsonParser = new JSONParser();
		
		if (source == null || source.isEmpty() || !source.contains(".json")) {
			System.out.println("[INFO]: file not specified or not in .json format, open default file: "
					+ DEFAULT_DIC_PATH);
			file = DEFAULT_DIC_PATH;
		}
		
		FileReader reader = readFile(file);
		
		// return false if cant find file and create new file failed
		if (reader == null) 
			return false;
		
		try {
            //Read JSON file
            dictData = (JSONObject) jsonParser.parse(reader);
            return true;
        } catch (IOException | ParseException e) {
        	System.err.println(e.getMessage());
            System.err.println("[ERROR]: parse json failed from file: " + file);
        }
		
		return false;
	}

	@Override
	public String processRequest(String request) {
		String respond = "";
		
		if (request == null || request.isEmpty())
			return respond;
		
		JSONParser jsonParser = new JSONParser();
		JSONObject requestJSON = new JSONObject();
		
		//Read JSON requst
		try {
			requestJSON = (JSONObject) jsonParser.parse(request);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
            System.err.println("[ERROR]: parse json failed from request: " + request);
            return "[ERROR]: invalid request";
		}
		
		// different action based on different command
		String command = requestJSON.get("command").toString();
		String word = requestJSON.get("word").toString();
		switch(command) {
			case "query":
				respond = queryWord(word);
				break;
			case "add":
				respond = addWord(word, requestJSON.get("meaning").toString());
				break;
			case "remove":
				respond = removeWord(word);
				break;
			default:
				respond = "[ERROR]: Unkown request command/type";
		}
		
		return request;
	}
	
	private FileReader readFile(String file) {
		FileReader reader = null;
		
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
            System.err.println("[ERROR]: can't find json file: " + file);
            
            try {
            	File newFile = new File(file);
            	newFile.createNewFile();
            	System.out.println("[INFO]: create new file at: " + file);
            	
            	// initialize data
            	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
            	dictData.put("createTime", formatter.format(new Date()));
            	dictData.put("modifiedTime", formatter.format(new Date()));
            	dictData.put("data", new JSONArray());
            	updateDic();
            	
            	reader = new FileReader(newFile);
            } catch (IOException e1) {
            	System.err.println(e1.getMessage());
                System.err.println("[ERROR]: can't create new json file or read from it: " + file);
            }
		}
		
		return reader;
	}
	
	private String queryWord(String word) {
		return word;
	}
	
	private String removeWord(String word) {
		
		if (updateDic()) {
			return "[INFO]: Successfully update server with removing";
		}
		else {
			return "[ERROR]: Update server with removing failed";
		}
	}
	
	private String addWord(String word, String meaning) {
		if (updateDic()) {
			return "[INFO]: Successfully update server with adding";
		}
		else {
			return "[ERROR]: Update server with adding failed";
		}
	}
	
	private boolean updateDic() {
		if (file == null || file.isEmpty() || dictData == null)
			return false;
		
		try (FileWriter writer = new FileWriter(file)) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			dictData.put("modifiedTime", formatter.format(new Date()));
			
			writer.write(dictData.toJSONString());
			System.out.println("[INFO]: Update json file successfully: " + file);
		} catch (Exception e) {
			System.err.println(e.getMessage());
            System.err.println("[ERROR]: can't write json to file: " + file);
		}
		
		return true;
	}
}
