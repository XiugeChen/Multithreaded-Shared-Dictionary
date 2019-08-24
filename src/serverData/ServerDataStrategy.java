package serverData;

public interface ServerDataStrategy {
	public abstract String processRequest(String request);
	public abstract boolean setDataSource(String source);
}
