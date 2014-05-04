
import java.net.UnknownHostException;
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
	private int my_id;
	private String my_host;

	public RMIClient(String the_host, int the_id) 
			throws RemoteException, NotBoundException, UnknownHostException {
		String name = "KVService";
		my_id = the_id;
		my_host = java.net.InetAddress.getLocalHost().getHostName();
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
				System.out.println(my_host + ": Thread " 
				+ my_id + ": put key1 value1");
				Thread.sleep(1000);
				kvs.put("key2", "value2");
				System.out.println(my_host + ": Thread " 
				+ my_id + ": put key2 value2");
				Thread.sleep(1000);
				kvs.put("key3", "value3");
				System.out.println(my_host + ": Thread " 
				+ my_id + ": put key3 value3");
				Thread.sleep(1000);
				kvs.put("key4", "value4");
				System.out.println(my_host + ": Thread " + my_id 
						+ ": put key4 value4");
				Thread.sleep(1000);
				kvs.put("key5", "value5");
				System.out.println(my_host + ": Thread " + my_id
						+ ": put key5 value5");
				Thread.sleep(1000);
				System.out.println(my_host + ": Thread " + my_id
						+ ": get key1; value = " + kvs.get("key1"));
				Thread.sleep(1000);
				System.out.println(my_host + ": Thread " + my_id 
						+ ": get key2; value = " + kvs.get("key2"));
				Thread.sleep(1000);
				System.out.println(my_host + ": Thread " + my_id 
						+ ": get key3; value = " + kvs.get("key3"));
				Thread.sleep(1000);
				System.out.println(my_host + ": Thread " + my_id
						+ ": get key4; value = " + kvs.get("key4"));
				Thread.sleep(1000);
				System.out.println(my_host + ": Thread " + my_id 
						+ ": get key5; value = " + kvs.get("key5"));
				Thread.sleep(1000);
				kvs.delete("key1");
				System.out.println(my_host + ": Thread " + my_id
						+ ": key1 deleted");
				Thread.sleep(1000);
				kvs.delete("key2");
				System.out.println(my_host + ": Thread " + my_id 
						+ ": key2 deleted");
				Thread.sleep(1000);
				kvs.delete("key3");
				System.out.println(my_host + ": Thread " + my_id 
						+ ": key3 deleted");
				Thread.sleep(1000);
				kvs.delete("key4");
				System.out.println(my_host + ": Thread " + my_id 
						+ ": key4 deleted");
				Thread.sleep(1000);
				kvs.delete(": key5");
				System.out.println(my_host + ": Thread " + my_id
						+ ": key5 deleted");
				Thread.sleep(1000);
				kvs.get("key1");
				System.out.println(my_host + ": Thread " + my_id 
						+ ": get key1; value = " + kvs.get("key1"));
				Thread.sleep(1000);
			}catch(Exception e){

			}
		}
	}

}



