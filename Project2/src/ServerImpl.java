/**
 * Created by Aqeel Bin Rustum on 4/21/14.
 */

import java.io.IOException;
import java.net.Inet4Address;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

class ServerImpl extends UnicastRemoteObject implements Server {
	private Logger logger ;
	private Map<String,String> kvalues;
	public ServerImpl() throws IOException {
		super();
		kvalues = new HashMap<String, String>();
		logger = new Logger("server.log");
		logger.log("RPC.Server start running on : " + Inet4Address.getLocalHost() , true);
	}
	public synchronized boolean put(String key, String value){
		kvalues.put(key, value);
		logger.log("Server call: put <" + key + "," + value + ">" , true);
		if(kvalues.containsKey(key)){
			logger.log(kvalues.toString() , true);
			return true;
		}
		else{
			return false;
		}
	}
	public synchronized String get(String key){
		logger.log("Server call: get (" + key + ")", true);
		String value = kvalues.get(key);
		logger.log("key = " + key + " , value = " + value, true);
		return value;
	}
	public synchronized boolean delete(String key){
		kvalues.remove(key);
		logger.log("Server call: delete(" + key + ")", true);
		if(!kvalues.containsKey(key)){
			logger.log("successful deletion of key = " + key, true);
			logger.log(kvalues.toString(), true);
			return true;
		}
		else{
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			if(args.length == 0){
				System.out.println("Error: no enough parameters");
				System.exit(2);
			}
			int port = Integer.parseInt(args[0]);

			//create a local instance of the object
			ServerImpl server = new ServerImpl();

			java.rmi.registry.LocateRegistry.createRegistry(port);

			//put the local instance in the registry
			String url = "//:" + port + "/RMISERVER";
			Naming.rebind(url, server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
