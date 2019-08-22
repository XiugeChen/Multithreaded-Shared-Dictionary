package clientApplication;

import org.kohsuke.args4j.CmdLineParser;

public class ClientApplication {
	
	ClientCmdValue cmdValues = null;
	
	public ClientApplication() {
		
	}
	
	public boolean setCmd(String[] args) {
		cmdValues = new ClientCmdValue(args);
	    CmdLineParser parser = new CmdLineParser(cmdValues);

	    try {
	        parser.parseArgument(args);
	    } catch (Exception e) {
	        return false;
	    }
		
	    System.out.println(cmdValues.getServerAddr() + cmdValues.getServerPort());
	    
		return true;
	}
}
