/*
 *Stephen Mosby
 *Kellen Han-Nin Cheng
 *Aqeel S Bin Rustum
 *Nai-Wei Chen
 *CSS558 Sp14 Project3
 */

import java.io.IOException;
import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

	private Map<Float,String> my_log;

	private int my_firstUnchosenIndex;
	
	private BlockingQueue<String> requests_queue;
	
	private int server_id;

	/**
	 * A simple constructor
	 * @throws IOExceptions
	 */
	public KVStore(int server_id) throws IOException{
		this.server_id = server_id;
		my_KVStore=new HashMap<String,String>();
		logger = new Logger("server.log");
		logger.log("RPC.Server start running on : " 
				+ Inet4Address.getLocalHost() , true);
		requests = new HashMap<String, String[]>();
		requests_queue = new LinkedBlockingQueue<String>();
		my_log = new HashMap<Float, String>();
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

	public void addRequest(String the_request){
		requests_queue.add(the_request);
		
	}

	public String pop(){
		return requests_queue.remove();
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
	synchronized public void put(String key, String value) 
			throws RemoteException{
<<<<<<< .mine
		String the_request = "put" + " " + key + " " + value;
		addRequest(the_request);
=======
		String val = "put "+the_key+" "+the_value+" f";
		

		my_KVStore.put(the_key, the_value);


>>>>>>> .r84
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

	private int minProposal;
	private int acceptedProposal;
	private String acceptedValue;

<<<<<<< .mine
	/*
	 * If an acceptor receives a prepare request with number n greater
	 * than that of any prepare request to which it has already responded,
	 * the it responds to the request with a promise not to accept any more
	 * proposals numbered less than n and with the highest-numbered proposal
	 * (if any) that it has accepted.
	 */
=======
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String checkForLeader(int server_id) {
		// TODO Auto-generated method stub
		return null;
	}

>>>>>>> .r82
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] prepare(float n) throws RemoteException {
		int round=(int) n;
		if(round>minProposal){
//			accept proposal if higher
			minProposal=round;
		}
//		assumption: if the returned proposal from the proposer is EQUAL the
//		proposed value, it is essentially a commitment
		String[] accepted={acceptedProposal+"",acceptedValue};
		return accepted;
	}

	/*
	 * If an acceptor receives an accept request for a proposal numbered n, it
	 * accepts the proposal unless it has already responded to a prepare request
	 * have a number greater than n.
	 */
	@Override
	public int accept(float n, String value)
			throws RemoteException {
		int round=(int)n;
		if(round>=minProposal){
			acceptedProposal=round;
			minProposal=round;
			acceptedValue=value;
		}
		return minProposal;
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