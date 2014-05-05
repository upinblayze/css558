import java.io.IOException;
import java.rmi.NotBoundException;

public class ClientMain {

	private static int NUM_CLIENTS = 2;

	public static void main(String[] args) 
			throws NotBoundException, IOException {
		if(args.length<1){
			System.out.println("Usage: java ClientMain <host>");
			System.exit(0);
		}

		for(int client_id = 1; client_id <= NUM_CLIENTS; client_id++) {
			(new Thread(new RMIClient(args[0],client_id))).start();
		}
	}

}
