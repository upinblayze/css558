/*
 *Stephen Mosby
 *Kellen Han-Nin Cheng
 *Aqeel S Bin Rustum
 *Nai-Wei Chen
 *CSS558 Sp14 Project2
*/

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple class that implements a KVService, allowing for client machines to 
 * execute methods on a KVStore.
 *
 */
public class RMIServer {

	private List<String> my_replicated_servers;
	
	public RMIServer() {
		my_replicated_servers = new ArrayList<String>();
	}
	
	public static void main(String[] args) {
		
		if(args.length < 1) {
			System.out.println("Usage: RMIServer <unique_server_name>");
			System.exit(0);
		}

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
