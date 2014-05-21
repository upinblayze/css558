/*
 *Stephen Mosby
 *Kellen Han-Nin Cheng
 *Aqeel S Bin Rustum
 *Nai-Wei Chen
 *CSS558 Sp14 Project2
*/

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * A simple class that implements a KVService, allowing for client machines to 
 * execute methods on a KVStore.
 *
 */
public class RMIServer {


	public static void main(String[] args) {
		
		try{
			String name = "KVService";
			String local_name = Inet4Address.getLocalHost().toString();
			KVStore kvs = new KVStore();
			KVService stub = (KVService)UnicastRemoteObject.exportObject(kvs,0);
			RMItwophasecommit stub2 = 
					(RMItwophasecommit)UnicastRemoteObject.exportObject(kvs,0);
			Registry reg = LocateRegistry.createRegistry(1099);
			reg.rebind(name, stub);
			reg.rebind(local_name, stub2);
			System.out.println("Server ready");
		}catch(Exception e){
			System.out.println("RMIServer error: "+e.getMessage());
			System.exit(0);
		}
	}

}
