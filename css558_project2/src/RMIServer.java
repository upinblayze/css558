import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class RMIServer implements KVService {
	HashMap<String,String> KVStore;


	RMIServer() throws RemoteException{
		KVStore=new HashMap<String,String>();
	}

	@Override
	public String get(String the_key) throws RemoteException{
		// TODO Auto-generated method stub
		return KVStore.get(the_key);
	}

	@Override
	public void put(String the_key, String the_value) throws RemoteException{
		// TODO Auto-generated method stub
		KVStore.put(the_key, the_value);
	}

	@Override
	public void delete(String the_key) throws RemoteException{
		// TODO Auto-generated method stub
		KVStore.remove(the_key);
	}
	
	
	
	public static void main(String[] args) {
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new SecurityManager());
		}
		try{
			String name = "KVService";
			KVService server = new RMIServer();
			KVService stub = (KVService)UnicastRemoteObject.exportObject(server,0);
			Registry reg = LocateRegistry.getRegistry();
			reg.rebind(name, stub);
			System.out.println("Server ready");
		}catch(Exception e){
			System.out.println("RMIServer error: "+e.getMessage());
			System.exit(0);
		}
	}

}
