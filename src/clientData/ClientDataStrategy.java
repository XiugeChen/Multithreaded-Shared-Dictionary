package clientData;

public interface ClientDataStrategy {
	public abstract String packData(String command, String word, String meaning);
	public abstract String resolveData(String data);
}
