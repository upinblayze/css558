import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIClient {

	public static void main(String[] args) {

		try{
			String name = "KVService";
			Registry registry = LocateRegistry.getRegistry(args[0]);
			KVService kvs = (KVService) registry.lookup(name);
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
