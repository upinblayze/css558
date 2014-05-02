import java.rmi.RemoteException;
import java.util.HashMap;


public class KVStore implements KVService {
	private HashMap<String,String> KVStore;


	public KVStore() throws RemoteException{
		KVStore=new HashMap<String,String>();
	}

	@Override
	synchronized public String get(String the_key) throws RemoteException{
		// TODO Auto-generated method stub
		return KVStore.get(the_key);
	}

	@Override
	synchronized public void put(String the_key, String the_value) throws RemoteException{
		// TODO Auto-generated method stub
		KVStore.put(the_key, the_value);
	}

	@Override
	synchronized public void delete(String the_key) throws RemoteException{
		// TODO Auto-generated method stub
		KVStore.remove(the_key);
	}
	
}
