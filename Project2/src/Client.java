/**
 * Created by Aqeel Bin Rustum on 4/22/14.
 */

import java.io.IOException;
import java.net.Inet4Address;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client
{
	private Logger logger ;
	private Server server;
	public Client(Server server) throws IOException {
		this.server = server;
		String timestamp = Logger.getTimestamp();
		logger = new Logger("client-" + timestamp + ".log");
		logger.log("Client is running on : " + Inet4Address.getLocalHost(), true);
	}

	public void prompt() throws RemoteException {
		Scanner input = new Scanner(System.in);
		String [] request = null;
		while(true){
			System.out.print("Enter your request: ");
			request = input.nextLine().trim().split("\\s+");
			if(request.length >= 2){
				if(request[0].toLowerCase().equals("put")){
					if(request.length == 3){
						logger.log("Request: " + request[0].toLowerCase() + " (" + request[1] + "," + request[2] + ")", true);
						server.put(request[1], request[2]);
					}
					else{
						System.out.println("Invalid request");
					}
				}
				else if(request[0].toLowerCase().equals("get")){
					logger.log("Request: " + request[0].toLowerCase() + " (" + request[1] + ")", true);
					String value = server.get(request[1]);
					logger.log("key = " + request[1] + " , value = " + value, true);

				}
				else if(request[0].toLowerCase().equals("delete")){
					logger.log("Request: " + request[0].toLowerCase() + " (" + request[1] + ")", true);
					server.delete(request[1]);
				}
			}
			else if(request.length == 1 && request[0].toLowerCase().equals("exit")){
				System.exit(0);
				input.close();
			}
			else{
				System.out.println("Invalid request");
			}
		}
	}
	public static void main(String []args)
	{
		if(args.length < 2){
			System.out.println("Error: no enough parameters");
			System.exit(2);
		}
		String serverName = args[0];
		int serverPort = Integer.parseInt(args[1]);
		try {
			String name = "rmi://localhost:" + serverPort + "/" + serverName;
			Server server = (Server) Naming.lookup(name);
			Client client = new Client(server);
			client.prompt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
