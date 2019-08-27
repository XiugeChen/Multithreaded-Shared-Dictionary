package serverData;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public interface ServerDataStrategy {
	public abstract String processRequest(String request);
	public abstract boolean setDataSource(String source);
}
