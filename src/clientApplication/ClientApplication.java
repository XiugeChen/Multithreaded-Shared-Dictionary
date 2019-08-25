package clientApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.kohsuke.args4j.CmdLineParser;

import clientData.ClientDataStrategyFactory;

public class ClientApplication {
	
	public static final String EXIT_COMMAND = "EXIT";
	
	ClientCmdValue cmdValues = null;
	Socket socket = null;
	
	// Input Stream
	DataInputStream input;
	// Output Stream
	DataOutputStream output;
	
	public ClientApplication() {
	}
	
	public String queryWord(String word) {
		if (socket == null || socket.isClosed()) {
			if (!setConnection())
				return "[Error]: No server connection";
		}
		
		String request = ClientDataStrategyFactory.getInstance().getJsonStrategy()
				.packRequest("query", word, "");
		
		return sendRequest(request);
	}
	
	public String addWord(String word, String meaning) {
		if (socket == null || socket.isClosed()) {
			if (!setConnection())
				return "[Error]: No server connection";
		}
		
		String request = ClientDataStrategyFactory.getInstance().getJsonStrategy()
				.packRequest("add", word, meaning);
		
		return sendRequest(request);
	}
	
	public String removeWord(String word) {
		if (socket == null || socket.isClosed()) {
			if (!setConnection())
				return "[Error]: No server connection";
		}
		
		String request = ClientDataStrategyFactory.getInstance().getJsonStrategy()
				.packRequest("remove", word, "");
		
		return sendRequest(request);
	}
	
	public void exit() {
		if (socket != null && !socket.isClosed())
			sendRequest(EXIT_COMMAND);
		
		System.exit(1);
	}
	
	public boolean setConnection() {
		if (!cmdValues.isErrorFree())
			return false;
		
		try {
			socket = new Socket(cmdValues.getServerAddr(), 
				Integer.parseInt(cmdValues.getServerPort()));
			
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
		    return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("[INFO]: Set up connection with server failed");
			return false;
		}
	}
	
	public boolean setCmd(String[] args) {
		cmdValues = new ClientCmdValue(args);
	    CmdLineParser parser = new CmdLineParser(cmdValues);

	    try {
	        parser.parseArgument(args);
	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	        return false;
	    }
		
		return true;
	}
	
	private String sendRequest(String request) {
		try {
			output.writeUTF(request);
			output.flush();
			
			if (request.equals(EXIT_COMMAND)) {
				return "";
			}
			else {
				return input.readUTF();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("[Error]: Send to or recieve from server failed");
			
			try {
				socket.close();
			} catch (IOException e1) {
				System.err.println(e1.getMessage());
				System.err.println("[Error]: Close socket failed");
			}
			
			return "[Error]: Send request to server failed";
		}
	}
}
