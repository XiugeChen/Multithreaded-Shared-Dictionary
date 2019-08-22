package clientApplication;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ClientCmdValue {
	private static final int USAGE_WIDTH = 100;
	
	private static final int SMALLEST_PORT = 1025;
	private static final int LARGEST_PORT = 65535;
	
	@Option(name = "-a", aliases = { "--address" }, required = true,
            usage = "input server address")
	private String serverAddr;
	
	@Option(name = "-p", aliases = { "--port" }, required = true,
            usage = "input server port")
	private String serverPort;
	
	private boolean errorFree = false;
	
	public ClientCmdValue(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		
        parser.setUsageWidth(USAGE_WIDTH);
        
        try {
            parser.parseArgument(args);
            
            if (!isValidAddr()) {
            	throw new CmdLineException(parser,
                        "--Input address are not valid.");
            }

            if (!isValidPort()) {
            	throw new CmdLineException(parser,
                        "--Input port are not valid.");
            }

            errorFree = true;
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        	parser.printUsage(System.err);
        	System.err.println("Correct format: java -jar DictionaryClient.jar -a <server_address> -p <port>");
        	System.exit(1);
        }
	}
	
	/**
     * Returns whether the parameters could be parsed without an
     * error.
     *
     * @return true if no error occurred.
     */
    public boolean isErrorFree() {
        return errorFree;
    }
    
    private boolean isValidAddr() {
    	return true;
    }
    
    private boolean isValidPort() {
    	try {
    		int port = Integer.parseInt(serverPort); 
    	    
    	    if (port <= LARGEST_PORT && port >= SMALLEST_PORT)
    	    	return true;
    	    else {
    	    	System.err.println("Port number should be some number between "
    	    			+ SMALLEST_PORT + " and " +  LARGEST_PORT);
    	    	return false;
    	    }
    	  } catch(Exception e){
    		  System.err.println(e.getMessage());
    		  System.err.println("Port number should be some number between "
  	    			+ SMALLEST_PORT + " and " +  LARGEST_PORT);
    		  return false;  
    	  }
    }

    // getters
    public String getServerAddr() {
        return serverAddr;
    }
    
    public String getServerPort() {
    	return serverPort;
    }
}
