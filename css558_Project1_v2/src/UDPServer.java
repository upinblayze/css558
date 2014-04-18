import java.io.IOException;
import java.net.SocketException;


public class UDPServer {

	public static void main(String[] args) throws IOException {
		new UDPServerThread().start();
	}

}
