
import java.io.IOException;
import java.net.Inet4Address;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



public class RMIClient  implements Runnable {
	private int SLEEP_TIME = 1000;
	private KVService kvs;
	private int my_id;
	private String my_host;
	private Logger logger;

	public RMIClient(String the_host, int the_id) 
			throws NotBoundException, IOException {
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
		String timestamp = Logger.getTimestamp();
		logger = new Logger("Client-" + the_id +"-" + timestamp + ".log");
		logger.log("Client " + the_id + " is running on : " + Inet4Address.getLocalHost(), true);
	}
	
	private void put(final String the_key, final String the_value) 
			throws RemoteException, InterruptedException {
		kvs.put(the_key,the_value);
		System.out.println(my_host + ": Thread " 
				+ my_id + ": put " + the_key + " " + the_value);
		logger.log("Client " + my_id 
				+ " has issued request: put("+the_key+","+the_value+")" , true);
		Thread.sleep(SLEEP_TIME);
	}

	private void get(final String the_key) 
			throws RemoteException, InterruptedException {
		String v;
		logger.log("Client " + my_id 
				+ " has issued request: get("+the_key+")" , true);
		v = kvs.get(the_key);
		System.out.println(my_host + ": Thread " + my_id
				+ ": get " + the_key + "; value = " + v);
		logger.log("Client " + my_id + " has got--> key = "
				+ the_key+ " , value = " + v, true);
		Thread.sleep(SLEEP_TIME);
	}
	
	private void delete(final String the_key)
			throws InterruptedException, RemoteException {
		kvs.delete(the_key);
		System.out.println(my_host + ": Thread " + my_id
				+ ": " + the_key + " deleted");
		logger.log("Client " + my_id 
				+ " has issued request: delete("+the_key+")" , true);
		Thread.sleep(1000);
	}
	
	
	@Override 
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try{
				put("key1", "value1");
				put("key2", "value2");
				put("key3", "value3");
				put("key4", "value4");
				put("key5", "value5");
				
				get("key1");
				get("key2");
				get("key3");
				get("key4");
				get("key5");
				
				delete("key1");
				delete("key2");
				delete("key3");
				delete("key4");
				delete("key5");
				
				get("key1");

			}catch(Exception e){

			}
		}
	}

}



