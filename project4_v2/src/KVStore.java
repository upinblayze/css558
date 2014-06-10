/*
 *Stephen Mosby
 *Kellen Han-Nin Cheng
 *Aqeel S Bin Rustum
 *Nai-Wei Chen
 *CSS558 Sp14 Project3
 */

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is a simple class that implements a key-value store.  Instances of this
 * class reside on the server.
 */
public class KVStore implements KVService, IPaxos {
	private static int QUORUM_COUNT = 3;
	
	private static int SERVER_COUNT = 5;
	
	private static int RETRIES = 5;

	/** The logger used to create and append to the server's log file. */
	private Logger logger;

	/**The hash map used to store and retrieve the key-value parings.*/
	private HashMap<String,String> KVStore;

	/**The hash map used to store and retrieve the key-value parings.*/
	private List<IPaxos> my_replicated_servers;

	/**The hash map used to store and retrieve the key-value parings.*/
	private Map<String, String[]> requests;

	private List<String> my_log = new ArrayList<String>();

	private int my_firstUnchosenIndex;

	public enum RequestType {ACK, GO}
	/**
	 * A simple constructor
	 * @throws IOExceptions
	 */
	public KVStore() throws IOException{
		KVStore=new HashMap<String,String>();
		logger = new Logger("server.log");
		logger.log("RPC.Server start running on : " 
				+ Inet4Address.getLocalHost() , true);
		requests = new HashMap<String, String[]>();
	}

	public List<IPaxos> getMy_replicated_servers() {
		return my_replicated_servers;
	}

	public void setMy_replicated_servers(
			List<IPaxos> my_replicated_servers) {
		this.my_replicated_servers = my_replicated_servers;
	}

	public Map<String, String[]> getRequests() {
		return requests;
	}

	synchronized public void addRequest(String the_request_id, String...the_request){
		requests.put(the_request_id, the_request);
	}

	synchronized public void deleteRequest(String the_request_id){
		requests.remove(the_request_id);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public String get(String the_key) throws RemoteException{
		logger.log("Server call: get (" + the_key + ")", true);
		//		System.out.println("Server call: get (" + the_key + ")");
		String value = KVStore.get(the_key);
		logger.log("key = " + the_key + " , value = " + value, true);
		//		System.out.println("key = " + the_key + " , value = " + value);
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public void put(String the_key, String the_value) 
			throws RemoteException{
		String val = the_value;
		int quorum_count = 0;
		String prep_reply;
		String[] prep_vals;

		if(my_log.get(my_firstUnchosenIndex) != null) {
			val = my_log.get(my_firstUnchosenIndex);
		}

		while(true) {
			//assume leader
			while (quorum_count < QUORUM_COUNT) {
				quorum_count = 0;
				//prepare phase
				for(IPaxos p: my_replicated_servers) {
					//prepare
					prep_reply = p.prepare(my_firstUnchosenIndex); //timeout??
					prep_vals = prep_reply.split("\\s+");
					if(prep_vals[0].equals('t') || prep_vals[0].equals('f') ) {
						quorum_count++;
					}
				}
			}
		
			//accept phase
			quorum_count = 0;
			int acceptorsFirstUnchosenIndex;
			for(IPaxos p: my_replicated_servers) {
				acceptorsFirstUnchosenIndex = 
						Integer.parseInt(p.accept(my_firstUnchosenIndex, val)); //timeouts??
				while(acceptorsFirstUnchosenIndex < my_firstUnchosenIndex) {
					acceptorsFirstUnchosenIndex = 
							Integer.parseInt(
									p.success(acceptorsFirstUnchosenIndex, 
									my_log.get(acceptorsFirstUnchosenIndex)));
				}
			}
			
			if(val.equals(the_value)) {
				break;
			} else {
				val = the_value;
			}
		}
		
		KVStore.put(the_key, the_value);

		//		
		//			KVStore.put(the_key, the_value);
		//			logger.log("Server call: put <" + the_key + "," 
		//					+ the_value + ">" , true);
		//
		//		
		//			logger.log("Server call: failed to do TPC -> put <" + the_key + "," 
		//					+ the_value + ">" , true);
		//		
		//		logger.log(KVStore.toString(),true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public void delete(String the_key) throws RemoteException{
		//		if(tpc(the_key)){
		//			if(!KVStore.containsKey(the_key)){
		//				logger.log("Key = " + the_key + " not found");
		//			} else {
		//				KVStore.remove(the_key);
		//				logger.log("Server call: delete(" + the_key + ")", true);
		//			}
		//			if(!KVStore.containsKey(the_key)){
		//				logger.log("successful deletion of key = " + the_key, true);
		//				logger.log(KVStore.toString(), true);
		//			} else{
		//				logger.log("failed deletion of key = " + the_key, true);
		//			}
		//		}
		//		else{
		//			logger.log("failed deletion of key = " + the_key + " due to failed TPC", true);
		//		}
		logger.log(KVStore.toString(),true);
	}

	private String generateId() throws UnknownHostException{
		return Inet4Address.getLocalHost() + "-" + Logger.getTimestamp();
	}

	@Override
	public int checkForLeader(int server_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String prepare(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String accept(int n, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String success(int index, String value) {
		// TODO Auto-generated method stub
		return null;
	}


}