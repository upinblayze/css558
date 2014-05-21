/*
 *Stephen Mosby
 *Kellen Han-Nin Cheng
 *Aqeel S Bin Rustum
 *Nai-Wei Chen
 *CSS558 Sp14 Project2
*/

import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * This is a simple class that implements a key-value store.  Instances of this
 * class reside on the server.
 */
public class KVStore implements KVService, RMItwophasecommit {
	/** The logger used to create and append to the server's log file. */
	private Logger logger;
	
	/**The hash map used to store and retrieve the key-value parings.*/
	private HashMap<String,String> mKVStore;

	/**List of all server host names to replicate on.*/
	private Set<String> my_hosts;
	
	/**My local hostname. */
	private String my_hostname;
	
	private List<RMItwophasecommit> my_handles;
	/**
	 * A simple constructor
	 * @throws IOException
	 * @throws NotBoundException 
	 */
	public KVStore() throws IOException, NotBoundException {
		mKVStore = new HashMap<String,String>();
		my_hosts = new HashSet<String>();
		my_hosts.addAll(Arrays.asList("n01","n02","n03","n04","n05"));
		my_hostname = (Inet4Address.getLocalHost()).toString();
		my_hosts.remove(my_hostname);
		my_handles = new ArrayList<RMItwophasecommit>();
		logger = new Logger("server.log");
		logger.log("RPC.Server start running on : " 
				+ Inet4Address.getLocalHost() , true);
		
		setUpHandles();
		
	}

	void setUpHandles() 
			throws MalformedURLException, RemoteException, NotBoundException {
		Iterator<String> itr = (Iterator<String>) my_hosts.iterator();
		while(itr.hasNext()) {
			String name = (String) itr.next();
			my_handles.add(
					(RMItwophasecommit) Naming.lookup("//"+name+"/"+name));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public String get(String the_key) throws RemoteException {
		logger.log("Server call: get (" + the_key + ")", true);
		System.out.println("Server call: get (" + the_key + ")");
		String value = mKVStore.get(the_key);
		logger.log("key = " + the_key + " , value = " + value, true);
		System.out.println("key = " + the_key + " , value = " + value);
		return mKVStore.get(the_key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public String put(String the_key, String the_value) 
			throws RemoteException{
		
		mKVStore.put(the_key, the_value);
		logger.log("Server call: put <" + the_key + "," 
				+ the_value + ">" , true);
		System.out.println("Server call: put <" + the_key + "," 
				+ the_value + ">" );
		return "ACK";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public String delete(String the_key) throws RemoteException{
		if(!mKVStore.containsKey(the_key)){
			logger.log("Key = " + the_key + " not found");
			System.out.println("Key = " + the_key + " not found");
		} else {
			mKVStore.remove(the_key);
			logger.log("Server call: delete(" + the_key + ")", true);
			System.out.println("Server call: delete(" + the_key + ")");
		}
		if(!mKVStore.containsKey(the_key)){
			logger.log("successful deletion of key = " + the_key, true);
			logger.log(mKVStore.toString(), true);
		} else{
			logger.log("failed deletion of key = " + the_key, true);
		}
		return "ACK";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String tpcPut(String the_request_id, String the_request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String tpcDelete(String the_request_id, String the_request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String tpcGO(String the_request_id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String tpcIsFresh(String the_version) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String tpcUpdate(KVStore the_KV_store) {
		// TODO Auto-generated method stub
		return null;
	}

}
