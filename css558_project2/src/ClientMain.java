import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ClientMain {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		if(args.length<1){
			System.out.println("Usage: java ClientMain <host>");
			System.exit(0);
		}
		(new Thread(new RMIClient(args[0]))).start();

		(new Thread(new RMIClient(args[0]))).start();
	}

}
