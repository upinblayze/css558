
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;


public class UDPServerThread extends Thread implements KVServiceProtocolInterface {

	private DatagramSocket my_socket;

	private DatagramPacket my_packet;

	private byte[] my_data;

	private Map<String, String> my_store;

	private String my_requested_val;

	private Logger my_log;

	public UDPServerThread() throws IOException {
		super("TCPServerThread");

		my_socket = new DatagramSocket(4445);
		my_store = new HashMap<String,String>();	
		my_log = new Logger("UDPServer_log.txt");
		my_log.write("UDPServer running!");
	}

	public void run() {
		while(true) {
			byte[] buf = new byte[256];

			//receive request
			my_packet = new DatagramPacket(buf, buf.length);
			my_data = buf;

			//process request
			try {
				processRequest();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}

	protected void close() {
		my_log.write("Closed!");
		my_log.close();
		my_socket.close();
	}

	@Override
	public void processRequest() throws IOException {
		my_socket.receive(my_packet);
		my_log.write("request recieved");
		Request r;
		String k = null, v = null;

		String data = new String(my_data);
		String delimiters = " ";
		String[] tokens = data.split(delimiters);

		if(tokens.length == 3) { 
			r = Request.valueOf(tokens[0]);
			k = tokens[1];
			v = tokens[2];
			switch (r) {
			case PUT:
				put(k,v);
				break;
			case DELETE:
				delete(k);
				break;
			case GET:
				get(k);
				break;
			default:
				//ERROR: Incorrect request format
				System.out.println("ERROR: Incorrect request format");
				break;
			}
			my_log.write(data);
		} else {
			System.out.println("ERROR: Incorrect request format");
		}

	}

	@Override
	public void put(final String the_key, final String the_value) {
		my_store.put(the_key, the_value);
	}

	@Override
	public void get(final String the_key) throws IOException {
		// TODO Auto-generated method stub
		my_requested_val = my_store.get(the_key);
		my_data = my_requested_val.getBytes();
		//send response
		InetAddress address = my_packet.getAddress();
		int port = my_packet.getPort();
		my_packet = new DatagramPacket(my_data, my_data.length, address, port);
		my_socket.send(my_packet);
	}

	@Override
	public void delete(final String the_key) {
		my_store.remove(the_key);
	}
}
