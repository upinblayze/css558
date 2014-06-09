import java.rmi.Remote;


public interface IPaxos extends Remote {
	
	int checkForLeader(int server_id);
	
	PrepVals prepare(int n);
	
	String accept(int n, int value);
	
	String success(int index, int value);
	
	
	class PrepVals {
		private boolean isNull;
		
		private boolean noMoreAccepted;
		
		private String my_value;
		
		public boolean isNull() {
			return isNull;
		}

		public void setNull(boolean isNull) {
			this.isNull = isNull;
		}

		public boolean isNoMoreAccepted() {
			return noMoreAccepted;
		}

		public void setNoMoreAccepted(boolean noMoreAccepted) {
			this.noMoreAccepted = noMoreAccepted;
		}

		public String getMy_value() {
			return my_value;
		}

		public void setMy_value(String my_value) {
			this.my_value = my_value;
		}
	}

}
