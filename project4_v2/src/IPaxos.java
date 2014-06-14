import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IPaxos extends Remote {
	
	
	public String[] prepare(float n) throws RemoteException;
	
	public int accept(float n, String value) throws RemoteException;
	
}
