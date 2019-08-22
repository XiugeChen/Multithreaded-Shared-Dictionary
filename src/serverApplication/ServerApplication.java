package serverApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.kohsuke.args4j.CmdLineParser;

public class ServerApplication {
	ServerCmdValue cmdValues = null;
	
	public ServerApplication() {
	}
	
	public void run() {
		if (!cmdValues.isErrorFree())
			return;
		
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try(ServerSocket server = factory.createServerSocket(Integer.parseInt(
				cmdValues.getServerPort()))) {
			
			System.out.println("[INFO]: Server start running at port: " + cmdValues.getServerPort());
			
			// Wait for connections.
			while(true) {
				Socket client = server.accept();
				
				System.out.println("[INFO]: accept client");
										
				// Start a new thread for a connection
				Thread t = new Thread(() -> {
					try {
						serveClient(client);
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}
				});
				
				t.start();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public boolean setCmd(String[] args) {
		cmdValues = new ServerCmdValue(args);
	    CmdLineParser parser = new CmdLineParser(cmdValues);

	    try {
	        parser.parseArgument(args);
	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	        return false;
	    }
	    
		return true;
	}
	
	private static void serveClient(Socket client) throws IOException {
		try(Socket clientSocket = client) {
			// Input stream
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
		    
		    String request = "";
		    
		    while (!request.equals("EXIT")) {
		    	request = input.readUTF();
		    	
		    	System.out.println("[INFO]: receive: "+ request);
		    	
		    	output.writeUTF(request);
			    output.flush();
		    }
		} 
		catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			System.out.println("[INFO]: end connection");
			client.close();
		}
	}
}
