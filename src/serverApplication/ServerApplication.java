package serverApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.net.ServerSocketFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kohsuke.args4j.CmdLineParser;

import serverData.ServerDataStrategy;
import serverData.ServerDataStrategyFactory;
import serverPresentation.ServerGUIControl;

public class ServerApplication<sychronized> {
	public static final String EXIT_COMMAND = "EXIT";
	
	ServerCmdValue cmdValues = null;
	
	CopyOnWriteArrayList<Socket> clients;
	
	ServerDataStrategy dataStrategy;
	
	ThreadPoolExecutor executor;
	
	public ServerApplication() {
		clients = new CopyOnWriteArrayList<>();
		
		dataStrategy = ServerDataStrategyFactory.getInstance().getJsonStrategy();
		
		int numThreads = Runtime.getRuntime().availableProcessors();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
		System.out.println("[INFO]: Thread pool with size " + numThreads + " created");
	}
	
	public void run() {
		// check command line values
		if (!cmdValues.isErrorFree())
			return;
		
		// check whether could open dictionary file correctly
		if (!dataStrategy.setDataSource(cmdValues.getDicPath()))
			return;
		
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try(ServerSocket server = factory.createServerSocket(Integer.parseInt(
				cmdValues.getServerPort()))) {
			
			System.out.println("[INFO]: Server start running at port: " + cmdValues.getServerPort());
			
			// Start a new thread for a connection
			Thread requestAssign = new Thread(() -> requestAssign());
			requestAssign.start();
			System.out.println("[INFO]: Start thread assignment thread");
			
			// Wait for connections.
			while(true) {
				Socket client = server.accept();
				System.out.println("[INFO]: accept client: " + client.getRemoteSocketAddress().toString()
				+ " " + client.getPort() + ", and bonded to local port: " + client.getLocalPort());
				
				ServerGUIControl.getInstance().addConnection(client.getRemoteSocketAddress().toString()
						, Integer.toString(client.getPort()));
					
				clients.add(client);
				System.out.println("[INFO]: add client " + client.getRemoteSocketAddress().toString() + " "
			    		+ client.getPort() + " to list");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println(e.getClass());
			System.err.println("[ERROR]: Fail accept or add new client to list");
		} finally {
			executor.shutdown();
			System.out.println("[INFO]: Thread pool closed");
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
	
	private void requestAssign() {
		while (true) {
			if (clients.size() == 0)
				continue;
			
			for (Socket client: clients) {
				// Input stream
				try {
					DataInputStream input = new DataInputStream(client.getInputStream());
				
					if (input.available() > 0) {
						String request = input.readUTF();
						RequestTask requestTask = new RequestTask(client, request);
					
						executor.execute(requestTask);
						System.out.println("[INFO]: start execute request task");
					}
				} catch (IOException e) {
					System.err.println(e.getMessage());
					System.err.println("[ERROR]: Read input stream from client fail");
				}
			}
		}
	}
	
	private class RequestTask implements Runnable {
		
		private Socket clientSocket = null;
		private String request = null;
		private boolean isClosed = false;
		
		public RequestTask(Socket clientSocket, String request) {
			this.clientSocket = clientSocket;
			this.request = request;
		}

		@Override
		public void run() {
			if (clientSocket == null || request == null) {
				System.err.println("Can not find socket or read stream of client");
				return;
			}
			
			try {
				// Output Stream
			    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
			    
			    System.out.println("[INFO]: receive: " + request + " from client: " 
			    		+ clientSocket.getRemoteSocketAddress().toString() + " "
			    		+ clientSocket.getPort() + ", at port: " + clientSocket.getLocalPort());
			    	
			    String respond = "NA";
			    if (!request.equals(EXIT_COMMAND)) {
			    	respond = dataStrategy.processRequest(request);
			    	
			    	output.writeUTF(respond);
				    output.flush();
			    }
			    else {
			    	isClosed = true;
			    	respond = "Close socket";
			    }
			    
			    String action = getAction(request);
			    ServerGUIControl.getInstance().addAction(clientSocket.getRemoteSocketAddress().toString()
		    			, Integer.toString(clientSocket.getPort()), action, respond);
			    
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.out.println("[ERROR]: client close socket without signal");
				isClosed = true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.out.println("[ERROR]: unexpected error occured");
				isClosed = true;
			} finally {
				if (isClosed) {
					clients.remove(clientSocket);
			    	System.out.println("[INFO]: remove client: " + clientSocket.getRemoteSocketAddress().toString() + " "
				    		+ clientSocket.getPort() + " from list");
			    	
			    	ServerGUIControl.getInstance().removeConnection(clientSocket.getRemoteSocketAddress().toString(), 
			    			Integer.toString(clientSocket.getPort()));
			    	
			    	try {
						clientSocket.close();
					} catch (IOException e) {
						System.err.println(e.getMessage());
						System.out.println("[ERROR]: close client socket failed");
					}
				}
			}
		}
		
		private String getAction(String request) {
			if (request.equals(EXIT_COMMAND))
				return EXIT_COMMAND;
			
			JSONParser jsonParser = new JSONParser();
			JSONObject requestJSON = new JSONObject();
			
			//Read JSON requst
			try {
				requestJSON = (JSONObject) jsonParser.parse(request);
			} catch (ParseException e) {
				System.err.println(e.getMessage());
	            System.err.println("[ERROR]: parse json failed from request: " + request);
	            return "Unable to identify action - " + request;
			}
			
			// different action based on different command
			String command = requestJSON.get("command").toString();
			String word = requestJSON.get("word").toString();
			return command + " - " + word;
		}
	}
}
