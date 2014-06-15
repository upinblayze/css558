import java.util.HashMap;
import java.util.Map;


public class Acceptor{
	private Map<Float, String> my_log;
	private float min_proposal_number;
	private String accepted_value;
	private float accepted_proposal_number;
	
	public Acceptor(final Map<Float, String> the_log){
		my_log = the_log;
		min_proposal_number = 0;
		accepted_value = null;
		accepted_proposal_number = 0;
	}
	
	/*
	 * If an acceptor receives a prepare request with number n greater
	 * than that of any prepare request to which it has already responded,
	 * the it responds to the request with a promise not to accept any more
	 * proposals numbered less than n and with the highest-numbered proposal
	 * (if any) that it has accepted.
	 */

	public String prepare(float n){
		String v;
		if(n > min_proposal_number){
			min_proposal_number = n;
			v = accepted_proposal_number + "," + accepted_value;
		}
		else{
			v = min_proposal_number + "";
		}
		return v;
	}

	/*
	 * If an acceptor receives an accept request for a proposal numbered n, it
	 * accepts the proposal unless it has already responded to a prepare request
	 * have a number greater than n.
	 */
	public String accept(float n, String value){
		if(n >= min_proposal_number){
			min_proposal_number = n;
			accepted_proposal_number = n;
			accepted_value = value;
			my_log.put(n, value+",[accepted]");
		}

		return min_proposal_number + "";
	}
}
