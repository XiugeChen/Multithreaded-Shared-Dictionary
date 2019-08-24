package clientData;

public interface ClientDataStrategy {
	public abstract String packRequest(String command, String word, String meaning);
	public abstract String resolveRespond(String data);
}
