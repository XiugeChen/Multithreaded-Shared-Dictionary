package serverApplication;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ServerCmdValue {
	private static final int USAGE_WIDTH = 100;
	
	private static final int SMALLEST_PORT = 1025;
	private static final int LARGEST_PORT = 65535;
	
	@Option(name = "-p", aliases = { "--port" }, required = true,
            usage = "input server port")
	private String serverPort;
	
	@Option(name = "-d", aliases = { "--dicPath" }, required = false,
            usage = "input dictionary file path(optinal)")
	private String dicPath;
	
	private boolean errorFree = false;
	
	public ServerCmdValue(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		
        parser.setUsageWidth(USAGE_WIDTH);
        
        try {
            parser.parseArgument(args);

            if (!isValidPort()) {
            	throw new CmdLineException(parser,
                        "--Input port are not valid.");
            }

            errorFree = true;
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        	parser.printUsage(System.err);
        	System.err.println("Correct format: java -jar DictionaryServer.jar "
        			+ "-p <port> -d <dictionary path(optional)>");
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
    public String getServerPort() {
    	return serverPort;
    }
    
    public String getDicPath() {
    	if (dicPath == null)
    		return "";
    	
    	return dicPath;
    }
}
