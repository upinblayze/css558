import java.util.Map;



public class LogProcessor implements Runnable {

	private Map<Float, String> my_log;
	
	private IKVProcessor my_kvs;

	private int earliestUnchosen;
	
	public LogProcessor(final Map<Float, String> the_log, 
			final IKVProcessor the_kvs) {
		my_log = the_log;
		my_kvs = the_kvs;
		earliestUnchosen = 0;
	}
	
	@Override
	public void run() {
		
		String proposal;
		String[] tokens;
		String[] request_tokens;
		while(true) {
			for(int i = earliestUnchosen; i < my_kvs.kv_size(); i++) {
				proposal = my_log.get(i);
				if(proposal != null) {
					tokens = proposal.split(",");
					if(tokens[2].equals("[chosen]")) {
						request_tokens = tokens[1].split("\\s+");
						if(request_tokens[0].equals("put")) {
							my_kvs.kv_put(request_tokens[1], request_tokens[2]);
						} else if (request_tokens[0].equals("delete")) {
							my_kvs.kv_delete(request_tokens[1]);
						}
					} else {
						earliestUnchosen = i;
						break;
					}
				}
			}
		}
	}

}
