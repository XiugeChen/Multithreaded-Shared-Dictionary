package clientApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineParser;

import clientData.ClientDataStrategyFactory;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ClientApplication {
	public static final String EXIT_COMMAND = "EXIT";
	private final static Logger logger = Logger.getLogger(ClientApplication.class);
	
	ClientCmdValue cmdValues = null;
	Socket socket = null;
	
	// Input and Output Stream
	DataInputStream input;
	DataOutputStream output;
	
	public ClientApplication() {
	}

	public String queryWord(String word) {
		if (isConnectServer()) {
			String request = ClientDataStrategyFactory.getInstance().getJsonStrategy()
				.packRequest("query", word, "");
		
			return sendRequest(request);
		}
		
		return "[Error]: No server connection";
	}
	
	public String addWord(String word, String meaning) {
		if (isConnectServer()) {
			String request = ClientDataStrategyFactory.getInstance().getJsonStrategy()
				.packRequest("add", word, meaning);
		
			return sendRequest(request);
		}
		
		return "[Error]: No server connection";
	}
	
	public String removeWord(String word) {
		if (isConnectServer()) {
			String request = ClientDataStrategyFactory.getInstance().getJsonStrategy()
				.packRequest("remove", word, "");
		
			return sendRequest(request);
		}
		
		return "[Error]: No server connection";
	}
	
	public void exit() {
		if (isConnectServer())
			sendRequest(EXIT_COMMAND);
	}
	
	public boolean setConnection() {
		if (!cmdValues.isErrorFree()) {
			logger.fatal("Command Line arguments not valid");
			return false;
		}
		
		try {
			socket = new Socket(cmdValues.getServerAddr(), 
				Integer.parseInt(cmdValues.getServerPort()));
			
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
		    return true;
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error("Set up connection with server failed");
			return false;
		}
	}
	
	public boolean setCmd(String[] args) {
		cmdValues = new ClientCmdValue(args);
	    CmdLineParser parser = new CmdLineParser(cmdValues);

	    try {
	        parser.parseArgument(args);
	    } catch (Exception e) {
	    	logger.fatal(e.toString());
	    	logger.fatal("Parse command line arguments failed");
	        return false;
	    }
		
	    logger.info("Parse command line arguments successfully");
		return true;
	}
	
	private String sendRequest(String request) {
		try {
			output.writeUTF(request);
			output.flush();
			logger.info("Send request to server successfully");
			
			if (request.equals(EXIT_COMMAND)) {
				return "";
			}
			else {
				String response = input.readUTF();
				logger.info("Recieve reponse from server successfully");
				return ClientDataStrategyFactory.getInstance().getJsonStrategy().resolveRespond(response);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			logger.error("Send to or recieve from server failed");
			
			try {
				socket.close();
				logger.info("Socket closed");
			} catch (IOException e1) {
				logger.error(e1.toString());
				logger.error("Close socket failed");
			}
			
			return "[Error]: Send request to server failed";
		}
	}
	
	private boolean isConnectServer() {
		if (socket == null || socket.isClosed()) {
			if (!setConnection()) {
				logger.error("No server connection");
				return false;
			}
		}
		return true;
	}
}
