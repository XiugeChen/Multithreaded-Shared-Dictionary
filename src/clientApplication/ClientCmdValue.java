package clientApplication;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public class ClientCmdValue {
	private final static Logger logger = Logger.getLogger(ClientCmdValue.class);
	
	private static final int USAGE_WIDTH = 100;
	private static final int SMALLEST_PORT = 1025;
	private static final int LARGEST_PORT = 65535;
	
	@Option(name = "-a", aliases = { "--address" }, required = false,
            usage = "input server address")
	private String serverAddr;
	
	@Option(name = "-p", aliases = { "--port" }, required = false,
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
        	logger.fatal(e.toString());
        	logger.fatal("Parse command line arguments failed, exit program");
        	parser.printUsage(System.err);
        	System.err.println("#### USAGE: Correct format: java -jar DictionaryClient.jar -a <server_address> -p <port>");
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
    	if (serverAddr == null) 
    		return true;
    	
    	return true;
    }
    
    private boolean isValidPort() {
    	if (serverPort == null) 
    		return true;
    		
    	try {
    		int port = Integer.parseInt(serverPort); 
    	    
    	    if (port <= LARGEST_PORT && port >= SMALLEST_PORT)
    	    	return true;
    	    else {
    	    	logger.fatal("Port number should be some number between "
    	    			+ SMALLEST_PORT + " and " +  LARGEST_PORT + ", instead of " + serverPort);
    	    	return false;
    	    }
    	  } catch(Exception e){
    		  logger.fatal(e.toString());
    		  logger.fatal("Port number should be some number between "
    				  + SMALLEST_PORT + " and " +  LARGEST_PORT + ", instead of " + serverPort);
    		  return false;  
    	  }
    }

    // getters
    public String getServerAddr() {
    	if (serverAddr == null)
    		return "localhost";
    	
        return serverAddr;
    }
    
    public String getServerPort() {
    	if (serverPort == null)
    		return "1111";
    	
    	return serverPort;
    }
}
