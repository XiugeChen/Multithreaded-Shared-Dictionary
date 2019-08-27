package clientData;

/**
 * @author Xiuge Chen (961392)
 * University of Melbourne
 * xiugec@student.unimelb.edu.au
 */
public interface ClientDataStrategy {
	public abstract String packRequest(String command, String word, String meaning);
	public abstract String resolveRespond(String data);
}
