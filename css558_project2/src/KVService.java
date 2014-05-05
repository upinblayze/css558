import java.rmi.Remote;
import java.rmi.RemoteException;


public interface KVService  extends Remote {

	String get(String the_key) throws RemoteException;
	void put(String the_key, String the_value) throws RemoteException;
	void delete(String the_key) throws RemoteException;
}
