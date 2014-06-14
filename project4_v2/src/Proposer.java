import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Proposer implements Runnable {
	private static final int QUORUM_SIZE = 3;
	private List<IPaxos> my_replicated_server;
	private BlockingQueue<String> requests_queue;
	private int server_id;
	private float proposal_number = 0;
	private Map<Float, String> my_log;

	public Proposer(int server_id, 
			final List<IPaxos> my_replicated_server,
			final BlockingQueue<String> requests_queue,
			final HashMap<Float, String> the_log) {
		this.server_id = server_id;
		proposal_number = server_id / 10;
		this.my_replicated_server = my_replicated_server;
		this.requests_queue = requests_queue;
		my_log = the_log;
	}

	@Override
	public void run() {
		float max_returned_proposal_number = 0;
		String returned_accepted_value;
		String[] prepare_response;
		float accept_response;
		int quorum_count = 0;
		while (true) {
			String request = null;
			if (!requests_queue.isEmpty()) {
				//prepare phase
				while (quorum_count < QUORUM_SIZE ) {
					quorum_count = 0;
					for (IPaxos p : my_replicated_server) {
						prepare_response = p.prepare(proposal_number);
						max_returned_proposal_number = Float.parseFloat(prepare_response[0]);
						returned_accepted_value = prepare_response[1];
						if(max_returned_proposal_number > proposal_number &&
								returned_accepted_value != null){
							proposal_number = max_returned_proposal_number;
							request = returned_accepted_value;
							quorum_count++;
						} 
					}
				}

				proposal_number++;

				if(request == null) {
					request = requests_queue.remove();
				}

				//accept phase
				quorum_count = 0;
				while (quorum_count < QUORUM_SIZE) {
					quorum_count = 0;
					for (IPaxos p : my_replicated_server) {
						accept_response = Float.parseFloat(p.accept(proposal_number, request));
						if(accept_response <= proposal_number) {
							quorum_count++;
						} 
					}
				}
				
			}
		}

	}

}
