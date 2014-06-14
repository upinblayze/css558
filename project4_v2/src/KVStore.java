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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

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
	private HashMap<String,String> my_KVStore;

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
		my_KVStore=new HashMap<String,String>();
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
		String value = my_KVStore.get(the_key);
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
		String val = "put "+the_key+" "+the_value+" f";
		

		my_KVStore.put(the_key, the_value);


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
		String val = "put "+the_key+" f";
		String temp = val;
		int quorum_count = 0;
		String prep_reply;
		String[] vals;

		if(my_log.get(my_firstUnchosenIndex) != null ) {
			temp = val;
			val = my_log.get(my_firstUnchosenIndex);
		}

		while(true) {
			//assume leader
			while (quorum_count < QUORUM_COUNT - 1) {
				quorum_count = 0;
				//prepare phase
				for(IPaxos p: my_replicated_servers) {
					//prepare
					prep_reply = p.prepare(my_firstUnchosenIndex); //timeout??
					vals = prep_reply.split("\\s+");
					if(vals[0].equals('t') || vals[0].equals('f') ) {
						quorum_count++;
					}
				}
			}

			//accept phase
			while (quorum_count < QUORUM_COUNT - 1) {
				quorum_count = 0;
				int acceptorsFirstUnchosenIndex;
				for(IPaxos p: my_replicated_servers) {
					acceptorsFirstUnchosenIndex = 
							Integer.parseInt(p.accept(my_firstUnchosenIndex, val)); //timeouts??
					quorum_count++;
					while(acceptorsFirstUnchosenIndex < my_firstUnchosenIndex) {
						acceptorsFirstUnchosenIndex = 
								Integer.parseInt(
										p.success(acceptorsFirstUnchosenIndex, 
												my_log.get(acceptorsFirstUnchosenIndex)));
					}
				}
			}

			if(quorum_count >= QUORUM_COUNT) {
				vals = val.split("\\s+");
				vals[vals.length-1] = "t";
				for(int i = 0; i < vals.length; i++) {
					val = vals[i] + " ";
				}
			}
			if(val.equals(temp)) {
				break;
			} else {
				val = temp;
			}
		}

		my_KVStore.remove(the_key);

		for(int i = my_firstUnchosenIndex; i < my_log.size(); i++) {
			temp = my_log.get(i);
			vals = temp.split("\\s+");
			if(vals[vals.length-1].equals('t')) {
				my_firstUnchosenIndex++;
			} else if (vals[vals.length-1].equals('t')) {
				my_firstUnchosenIndex = i;
				break;
			}
		}

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
		//		logger.log(my_KVStore.toString(),true);
	}

	private String generateId() throws UnknownHostException{
		return Inet4Address.getLocalHost() + "-" + Logger.getTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String checkForLeader(int server_id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String prepare(int n) {
		// TODO Auto-generated method stub
		String proposal=null+" F";
		if(n<my_log.size()){
			proposal=my_log.get(n);
		}else{
			//			catch the array up to proposal size
			while(my_log.size()<=n){
				my_log.add(proposal);
			}
		}
		return proposal;
	}

	@Override
	public String accept(int n, String value) {
		// TODO Auto-generated method stub
		boolean foundFirstIndex=false;
		int index=0;
		while(!foundFirstIndex && index<my_log.size()){
			String[] localVal=my_log.get(index).split(" ");
			if(localVal[localVal.length-1].equalsIgnoreCase("F")){
				foundFirstIndex=true;
			}
		}
		return index+"";
	}

	@Override
	public String success(int index, String value) {
		// TODO Auto-generated method stub

		my_log.set(index, value);
		return null;
	}

	
	private void paxos(String val) {
		int quorum_count = 0;
		String prep_reply;
		String[] vals;
		Stack<String> accepted_proposals = new Stack<String>();

		if(my_log.get(my_firstUnchosenIndex) != null) {
			accepted_proposals.add(val);
			val = my_log.get(my_firstUnchosenIndex);
		}

		while(!accepted_proposals.isEmpty()) {
			//assume leader
			while (quorum_count < QUORUM_COUNT - 1) {
				quorum_count = 0;
				//prepare phase
				for(IPaxos p: my_replicated_servers) {
					//prepare
					prep_reply = p.prepare(my_firstUnchosenIndex); //timeout??
					if(!prep_reply.equals("ACK")) {
						accepted_proposals.add(val);
						val = prep_reply;
					}
					quorum_count++;
				}
			}

			//accept phase
			quorum_count = 0;
			while (quorum_count < QUORUM_COUNT - 1) {
				quorum_count = 0;
				int acceptorsFirstUnchosenIndex;
				for(IPaxos p: my_replicated_servers) {
					acceptorsFirstUnchosenIndex = 
							Integer.parseInt(
									p.accept(my_firstUnchosenIndex, val)); //timeouts??
					quorum_count++;
					while(acceptorsFirstUnchosenIndex < my_firstUnchosenIndex) {
						acceptorsFirstUnchosenIndex = 
								Integer.parseInt(
										p.success(acceptorsFirstUnchosenIndex, 
												my_log.get(acceptorsFirstUnchosenIndex)));
					}
				}
			}

			//the value has been chosen so mark true
			vals = val.split("\\s+");
			vals[vals.length-1] = "t";
			for(int i = 0; i < vals.length; i++) {
				val = vals[i] + " ";
			}
		}
		
		//now find first unchosen
		String temp;
		for(int i = my_firstUnchosenIndex + 1; i < my_log.size(); i++) {
			temp = my_log.get(i);
			vals = temp.split("\\s+");
			if(vals[vals.length-1].equals('t')) {
				my_firstUnchosenIndex++;
			} else if (vals[vals.length-1].equals('t')) {
				my_firstUnchosenIndex = i;
				break;
			}
		}
	}
}