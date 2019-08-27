package serverData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ServerJsonDataStrategy implements ServerDataStrategy {
	private static final String DEFAULT_DIC_PATH = "resources/data/dictionary.json";
	private final static Logger logger = Logger.getLogger(ServerJsonDataStrategy.class);
	
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
			logger.info("File not specified or not in .json format, open default file: "
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
            logger.info("Open json file successfully");
            return true;
        } catch (IOException | ParseException e) {
        	logger.fatal(e.toString());
        	logger.fatal("Parse json failed from file: " + file);
        }
		
		logger.fatal("Open json file failed");
		return false;
	}

	@Override
	public String processRequest(String request) {
		String respond = "[ERROR]: Invalid request";
		
		if (request == null || request.isEmpty()) {
			logger.error("Recieve invalid request: " + request);
			return respond;
		}
		
		JSONParser jsonParser = new JSONParser();
		JSONObject requestJSON = new JSONObject();
		
		//Read JSON requst
		try {
			requestJSON = (JSONObject) jsonParser.parse(request);
		} catch (ParseException e) {
			logger.error(e.toString());
			logger.error("Parse json failed from request: " + request);
            return respond;
		}
		
		// different action based on different command
		String command = requestJSON.get("command").toString();
		String word = requestJSON.get("word").toString();
		Boolean isValid = Boolean.parseBoolean(requestJSON.get("isValid").toString());
		
		if (!isValid) {
			logger.info("Recieve invalid tag request from user");
			return respond;
		}
		
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
				logger.error("Unkown request common/type from user");
				respond = "[ERROR]: Unkown request command/type";
		}
		
		return respond;
	}
	
	@SuppressWarnings("unchecked")
	private FileReader readFile(String file) {
		FileReader reader = null;
		
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			logger.error(e.toString());
			logger.error("can't find json file: " + file);
            
            try {
            	File newFile = new File(file);
            	newFile.createNewFile();
            	logger.info("Create new file at: " + file);
            	
            	// initialize data
            	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
            	dictData.put("createTime", formatter.format(new Date()));
            	dictData.put("modifiedTime", formatter.format(new Date()));
            	dictData.put("data", new JSONArray());
            	updateDic();
            	
            	reader = new FileReader(newFile);
            } catch (IOException e1) {
            	logger.error(e1.toString());
            	logger.error("Can't create new json file or read from it: " + file);
            }
		}
		
		return reader;
	}
	
	private String queryWord(String word) {
		logger.info("Recieve query request of word: " + word);
		
		String response = "[INFO]: Can not find word: " + word + " on server";
		JSONArray pairs = (JSONArray) dictData.get("data");
		
		for (int i = 0; i < pairs.size(); i++) {
			JSONObject pair = (JSONObject) pairs.get(i);
			
			if (pair.get("word").toString().equals(word.toLowerCase())) {
				response = pair.get("meaning").toString();
				break;
			}
		}

		return response;
	}
	
	@SuppressWarnings("unchecked")
	private  String removeWord(String word) {
		logger.info("Recieve remove request of word: " + word);
		
		JSONArray pairs = (JSONArray) dictData.get("data");
		int i = 0;
		
		for (i = 0; i < pairs.size(); i++) {
			JSONObject pair = (JSONObject) pairs.get(i);
			
			if (pair.get("word").toString().equals(word.toLowerCase())) {
				break;
			}
		}
		
		if (i < pairs.size()) {
			pairs.remove(i);
			dictData.put("data", pairs);
			
			if (updateDic()) {
				return "[INFO]: Successfully update server with removing";
			}
			else {
				return "[ERROR]: Update server with removing failed";
			}
		}
		else {
			return "[INFO]: Can not find word " + word + " on server";
		}
	}
	
	@SuppressWarnings("unchecked")
	private String addWord(String word, String meaning) {
		logger.info("Recieve add request of word: " + word + ", with meaning: " + meaning);
		
		JSONArray pairs = (JSONArray) dictData.get("data");
		boolean wordExist = false;
		
		for (int i = 0; i < pairs.size(); i++) {
			JSONObject pair = (JSONObject) pairs.get(i);
			
			if (pair.get("word").toString().equals(word.toLowerCase())) {
				wordExist = true;
				logger.warn("New word \"" + word + "\" already exist in the dictionary");
				break;
			}
		}
		
		if (!wordExist) {
			JSONObject newWord = new JSONObject();
			newWord.put("meaning", meaning);
			newWord.put("word", word.toLowerCase());
			pairs.add(newWord);
			
			if (updateDic()) {
				return "[INFO]: Successfully update server with adding";
			}
			else {
				return "[ERROR]: Update server with adding failed";
			}
		}
		else {
			return "[INFO]: Word " + word + " already exists in the server";
		}
	}
	
	@SuppressWarnings("unchecked")
	private synchronized boolean updateDic() {
		if (file == null || file.isEmpty() || dictData == null)
			return false;
		
		try (FileWriter writer = new FileWriter(file)) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			dictData.put("modifiedTime", formatter.format(new Date()));
			
			writer.write(dictData.toJSONString());
			System.out.println("[INFO]: Update json file successfully: " + file);
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error("Can't write json to file: " + file);
		}
		
		logger.info("Update dictionary successfully");
		return true;
	}
}
