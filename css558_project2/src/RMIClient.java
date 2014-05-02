
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class RMIClient  implements Runnable {
	private KVService kvs;
	
	public RMIClient(String the_host) throws RemoteException, NotBoundException {
		String name = "KVService";
//		Registry registry = LocateRegistry.getRegistry(the_host);
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
				System.out.println("put: key1 to value1");
				kvs.put("key1", "value1");
				System.out.println("put: key2 to value2");
				kvs.put("key2", "value2");
				System.out.println("put: key3 to value3");
				kvs.put("key3", "value3");
				System.out.println("put: key4 to value4");
				kvs.put("key4", "value4");
				System.out.println("put: key5 to value5");
				kvs.put("key5", "value5");
				System.out.println("retriving key1 "+kvs.get("key1"));
				System.out.println("retriving key2 "+kvs.get("key2"));
				System.out.println("retriving key3 "+kvs.get("key3"));
				System.out.println("retriving key4 "+kvs.get("key4"));
				System.out.println("retriving key5 "+kvs.get("key5"));
				System.out.println("deleting key1");
				kvs.delete("key1");
				System.out.println("deleting key2");
				kvs.delete("key2");
				System.out.println("deleting key3");
				kvs.delete("key3");
				System.out.println("deleting key4");
				kvs.delete("key4");
				System.out.println("deleting key5");
				kvs.delete("key5");
				System.out.println("retriving key1 "+kvs.get("key1"));
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

}
