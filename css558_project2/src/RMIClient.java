import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIClient  implements Runnable {
	private KVService kvs;
	
	public RMIClient(String the_host) throws RemoteException, NotBoundException {
		String name = "KVService";
		Registry registry = LocateRegistry.getRegistry(the_host);
	    kvs = (KVService) registry.lookup(name);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try{
				kvs.put("key1", "value1");
				kvs.put("key2", "value2");
				kvs.put("key3", "value3");
				kvs.put("key4", "value4");
				kvs.put("key5", "value5");
				kvs.get("key1");
				kvs.get("key2");
				kvs.get("key3");
				kvs.get("key4");
				kvs.get("key5");
				kvs.delete("key1");
				kvs.delete("key2");
				kvs.delete("key3");
				kvs.delete("key4");
				kvs.delete("key5");
				kvs.get("key1");
			}catch(Exception e){
				
			}
		}
	}

}
