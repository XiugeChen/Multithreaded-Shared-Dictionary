package clientApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.kohsuke.args4j.CmdLineParser;

public class ClientApplication {
	
	ClientCmdValue cmdValues = null;
	Socket socket = null;
	
	// Input Stream
	DataInputStream input;
	// Output Stream
	DataOutputStream output;
	
	
	public ClientApplication() {
	}
	
	public String queryWord(String word) {
		if (socket == null)
			return "[Error]: No server connection";
		
		return sendRequest(word);
	}
	
	public String addWord(String word, String meaning) {
		if (socket == null)
			return "[Error]: No server connection";
		
		return sendRequest(word + " " + meaning);
	}
	
	public String removeWord(String word) {
		if (socket == null)
			return "[Error]: Remove word, No server connection";
		
		return sendRequest(word);
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
			System.out.println(request);
		
			output.writeUTF(request);
			output.flush();
			
			return input.readUTF();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return "[Error]: Send request to server failed";
		}
	}
}
