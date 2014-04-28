import java.rmi.Remote;


public interface KVService  extends Remote {

	
	void get(String the_key);
	
	void put(String the_key, String the_value);
	
	void delete(String the_key);
}
