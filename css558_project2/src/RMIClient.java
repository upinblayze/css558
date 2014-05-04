
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class RMIClient  implements Runnable {
	private KVService kvs;
	
	public RMIClient(String the_host) throws RemoteException, NotBoundException {
		String name = "KVService";
//		Registry registry = LocateRegistry.getRegistry(the_host);
//		kvs=(KVService) registry.lookup(name);
		try{
			kvs = (KVService) Naming.lookup("//"+the_host+"/"+name);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try{
				kvs.put("key1", "value1");
				System.out.println("put key1 value1");
				Thread.sleep(1000);
				kvs.put("key2", "value2");
				System.out.println("put key2 value2");
				Thread.sleep(1000);
				kvs.put("key3", "value3");
				System.out.println("put key3 value3");
				Thread.sleep(1000);
				kvs.put("key4", "value4");
				System.out.println("put key4 value4");
				Thread.sleep(1000);
				kvs.put("key5", "value5");
				System.out.println("put key5 value5");
				Thread.sleep(1000);
				System.out.println("get key1; value = " + kvs.get("key1"));
				Thread.sleep(1000);
				System.out.println("get key2; value = " + kvs.get("key2"));
				Thread.sleep(1000);
				System.out.println("get key3; value = " + kvs.get("key3"));
				Thread.sleep(1000);
				System.out.println("get key4; value = " + kvs.get("key4"));
				Thread.sleep(1000);
				System.out.println("get key5; value = " + kvs.get("key5"));
				Thread.sleep(1000);
				kvs.delete("key1");
				System.out.println("delete key1");
				Thread.sleep(1000);
				kvs.delete("key2");
				System.out.println("delete key2");
				Thread.sleep(1000);
				kvs.delete("key3");
				System.out.println("delete key3");
				Thread.sleep(1000);
				kvs.delete("key4");
				System.out.println("delete key4");
				Thread.sleep(1000);
				kvs.delete("key5");
				System.out.println("delete key5");
				Thread.sleep(1000);
				kvs.get("key1");
				System.out.println("get key1; value = " + kvs.get("key1"));
				Thread.sleep(1000);
			}catch(Exception e){

			}
		}
	}

}



