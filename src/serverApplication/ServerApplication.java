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

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kohsuke.args4j.CmdLineParser;

import serverData.ServerDataStrategy;
import serverData.ServerDataStrategyFactory;
import serverPresentation.ServerGUIControl;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ServerApplication {
	public static final String EXIT_COMMAND = "EXIT";
	
	private final static Logger logger = Logger.getLogger(ServerApplication.class);
	
	ServerCmdValue cmdValues = null;
	
	CopyOnWriteArrayList<Socket> clients;
	
	ServerDataStrategy dataStrategy;
	
	ThreadPoolExecutor executor;
	
	public ServerApplication() {
		clients = new CopyOnWriteArrayList<>();
		
		dataStrategy = ServerDataStrategyFactory.getInstance().getJsonStrategy();
		
		int numThreads = Runtime.getRuntime().availableProcessors();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
		logger.info("Thread pool with size " + numThreads + " created");
	}
	
	public void run() {
		// check command line values
		if (!cmdValues.isErrorFree())  {
			logger.fatal("Command line arguments not valid");
			return;
		}
		
		// check whether could open dictionary file correctly
		if (!dataStrategy.setDataSource(cmdValues.getDicPath())) {
			logger.fatal("Dictionary path not reachable: " + cmdValues.getDicPath());
			return;
		}
		
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try(ServerSocket server = factory.createServerSocket(Integer.parseInt(
				cmdValues.getServerPort()))) {
			
			logger.info("Server start running at port: " + cmdValues.getServerPort());
			
			// Start a new thread for a connection
			Thread requestAssign = new Thread(() -> requestAssign());
			requestAssign.start();
			logger.info("Start thread assignment thread");
			
			// Wait for connections.
			while(true) {
				Socket client = server.accept();
				String clientAddr = client.getRemoteSocketAddress().toString();
				String clientPort = Integer.toString(client.getPort());
				
				logger.info("Accept client: " + clientAddr + " " + clientPort 
						+ ", and bonded to local port: " + client.getLocalPort());
				
				ServerGUIControl.getInstance().addConnection(clientAddr, clientPort);
					
				clients.add(client);
				logger.info("Add client " + clientAddr + " " + clientPort + " to list");
			}
		} catch (Exception e) {
			logger.fatal(e.toString());
			logger.fatal("Fail accept or add new client to list");
		} finally {
			executor.shutdown();
			logger.info("Thread pool closed");
		}
	}
	
	public void exit() {
		if (executor != null) {
			executor.shutdown();
			logger.info("Thread pool closed");
		}
		
		if (clients != null) {
			for (Socket client: clients) {
				try {
					client.close();
				} catch (IOException e) {
					logger.error(e.toString());
					logger.error("Close client socket failed: " + client.getRemoteSocketAddress().toString()
							+ " " + client.getPort());
				}
			}
		}
	}
	
	public boolean setCmd(String[] args) {
		cmdValues = new ServerCmdValue(args);
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
	
	private void requestAssign() {
		while (true) {
			if (clients.size() == 0)
				continue;
			
			for (Socket client: clients) {
				String clientAddr = client.getRemoteSocketAddress().toString();
				String clientPort = Integer.toString(client.getPort());
				
				// Input stream
				try {
					DataInputStream input = new DataInputStream(client.getInputStream());
				
					if (input.available() > 0) {
						String request = input.readUTF();
						RequestTask requestTask = new RequestTask(client, request);
					
						executor.execute(requestTask);
						logger.info("Start execute request task from: " + clientAddr + " " + clientPort);
					}
				} catch (IOException e) {
					logger.error(e.toString());
					logger.error("Read input stream from client fail: " + clientAddr + " " + clientPort);
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
				logger.error("Can not find socket or read stream of client");
				return;
			}
			
			String clientAddr = clientSocket.getRemoteSocketAddress().toString();
			String clientPort = Integer.toString(clientSocket.getPort());
			
			try {
				// Output Stream
			    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
			    
			    logger.info("Receive: " + request + " from client: " + clientAddr + " " 
			    		+ clientPort + ", at port: " + clientSocket.getLocalPort());
			    	
			    String respond = "NA";
			    if (!request.equals(EXIT_COMMAND)) {
			    	respond = dataStrategy.processRequest(request);
			    	
			    	output.writeUTF(respond);
				    output.flush();
				    logger.info("Send response to client: " + clientAddr + " " 
				    		+ clientPort + " successfully");
			    }
			    else {
			    	isClosed = true;
			    	respond = "Close socket";
			    	logger.info("Client " + clientAddr + " " + clientPort + " ask to close socket");
			    }
			    
			    String action = getAction(request);
			    ServerGUIControl.getInstance().addAction(clientAddr, clientPort, action, respond);
			    
			} catch (IOException e) {
				logger.error(e.toString());
				logger.error("Client " + clientAddr + " " + clientPort + " close socket without signal");
				isClosed = true;
			} catch (Exception e) {
				logger.error(e.toString());
				logger.error("Unexpected error occured while read request and send response from: "
						+ clientAddr + " " + clientPort);
				isClosed = true;
			} finally {
				if (isClosed) {
					clients.remove(clientSocket);
			    	logger.info("Remove client: " + clientAddr + " " + clientPort + " from list");
			    	
			    	ServerGUIControl.getInstance().removeConnection(clientSocket.getRemoteSocketAddress().toString(), 
			    			Integer.toString(clientSocket.getPort()));
			    	
			    	try {
						clientSocket.close();
					} catch (IOException e) {
						logger.error(e.toString());
						logger.error("Close client socket failed: " + clientAddr + " " + clientPort);
					}
				}
			}
		}
		
		private String getAction(String request) {
			if (request.equals(EXIT_COMMAND))
				return EXIT_COMMAND;
			
			String clientAddr = clientSocket.getRemoteSocketAddress().toString();
			String clientPort = Integer.toString(clientSocket.getPort());
			
			JSONParser jsonParser = new JSONParser();
			JSONObject requestJSON = new JSONObject();
			
			//Read JSON requst
			try {
				requestJSON = (JSONObject) jsonParser.parse(request);
			} catch (ParseException e) {
				logger.error(e.toString());
	            logger.error("Parse json failed from request: " + request + ". Client: " + clientAddr 
	            		+ " " + clientPort);
	            return "Unable to identify action - " + request;
			}
			
			// different action based on different command
			String command = requestJSON.get("command").toString();
			String word = requestJSON.get("word").toString();
			return command + " - " + word;
		}
	}
}
