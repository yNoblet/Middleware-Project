package interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Topic extends UnicastRemoteObject implements ITopic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3180799191650643596L;
	private String title;
	private Set<String> client_list;
	private ArrayList<Message> historic;
	private Set<IClient> connected_clients;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<String> getClient_list() {
		return client_list;
	}
	public void setClient_list(Set<String> client_list) {
		this.client_list = client_list;
	}
	public ArrayList<Message> getHistoric() {
		return historic;
	}
	public void setHistoric(ArrayList<Message> historic) {
		this.historic = historic;
	}
	
	public Topic(String title) throws RemoteException {
		super();
		this.title = title;
		this.client_list = new HashSet<String>();
		this.historic = new ArrayList<Message>();
		this.connected_clients = new HashSet<IClient>();
	}
	
	public String getHistoricString(){
		String hist = "";
		for(Message msg : historic){
			hist += msg.toString()+"\n";
		}
		return hist;
	}
	
	@Override
	public void subscribe(String pseudo) throws RemoteException {
		client_list.add(pseudo);
	}
	@Override
	public void unsubscribe(String pseudo) throws RemoteException {
		client_list.remove(pseudo);
	}
	@Override
	public void post(String pseudo, String message) throws RemoteException {
		System.out.println(pseudo+": "+message);
		historic.add(new Message(pseudo, message));
	}
	@Override
	public void connect_client(IClient cl) throws RemoteException {
		connected_clients.add(cl);
		cl.refresh(getHistoricString());
	}
	@Override
	public void deconnect_client(IClient cl) throws RemoteException {
		connected_clients.remove(cl);
	}
}
