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

import org.kohsuke.args4j.CmdLineParser;

import serverData.ServerDataStrategy;
import serverData.ServerDataStrategyFactory;

public class ServerApplication {
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
				+ " " + client.getPort() + ", and bonded to local port: " + client.getLocalPort() );
					
				getClients().add(client);
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
	
	public CopyOnWriteArrayList<Socket> getClients() {
		return clients;
	}
	
	private void requestAssign() {
		while (true) {
			if (getClients().size() == 0)
				continue;
			
			for (Socket client: getClients()) {
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
			    	
			    if (!request.equals(EXIT_COMMAND)) {
			    	String respond = dataStrategy.processRequest(request);
			    	
			    	output.writeUTF(respond);
				    output.flush();
			    }
			    else {
			    	isClosed = true;
			    }
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
					getClients().remove(clientSocket);
			    	System.out.println("[INFO]: remove client: " + clientSocket.getRemoteSocketAddress().toString() + " "
				    		+ clientSocket.getPort() + " from list");
			    	try {
						clientSocket.close();
					} catch (IOException e) {
						System.err.println(e.getMessage());
						System.out.println("[ERROR]: close client socket failed");
					}
				}
			}
		}
	}
}
