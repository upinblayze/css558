import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class RMIServer {

	public static void main(String[] args) {

		try{
			String name = "KVService";
			KVStore kvs = new KVStore();
			KVService stub = (KVService)UnicastRemoteObject.exportObject(kvs,0);
			Registry reg = LocateRegistry.createRegistry(1099);
			reg.rebind(name, stub);
			System.out.println("Server ready");
		}catch(Exception e){
			System.out.println("RMIServer error: "+e.getMessage());
			System.exit(0);
		}
	}

}
