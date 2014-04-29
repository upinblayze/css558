import java.rmi.Naming;
import java.rmi.RMISecurityManager;


public class RMIClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setSecurityManager(new RMISecurityManager());
		try{
			KVService kv= (KVService) Naming.lookup("n01");
		}catch(Exception e){
			
		}
	}

}
