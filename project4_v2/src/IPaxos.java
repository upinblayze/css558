import java.rmi.Remote;


public interface IPaxos extends Remote {
	
	int checkForLeader(int server_id);
	
	String prepare(int n);
	
	String accept(int n, int value);
	
	String success(int index, int value);

}
