package core;

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
	private String author;
	private Set<String> clientList;
	private ArrayList<Message> historic;
	private Set<IClient> connectedClients;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Set<String> getClient_list() {
		return clientList;
	}
	public void setClient_list(Set<String> client_list) {
		this.clientList = client_list;
	}
	public ArrayList<Message> getHistoric() {
		return historic;
	}
	public void setHistoric(ArrayList<Message> historic) {
		this.historic = historic;
	}
	
	public Topic(String title, String author) throws RemoteException {
		super();
		this.title = title;
		this.author = author;
		this.clientList = new HashSet<String>();
		this.clientList.add(author);
		this.historic = new ArrayList<Message>();
		this.connectedClients = new HashSet<IClient>();
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
		clientList.add(pseudo);
	}
	@Override
	public void unsubscribe(String pseudo) throws RemoteException {
		clientList.remove(pseudo);
	}
	@Override
	public void post(String pseudo, String message) throws RemoteException {
		Message msg = new Message(pseudo, message);
		historic.add(msg);
		notifyMembers(msg.toString());
	}
	@Override
	public void connectClient(IClient cl) throws RemoteException {
		connectedClients.add(cl);
		cl.addConnectedTopic(this);
		cl.refresh(getHistoricString());
	}
	@Override
	public void disconnectClient(IClient cl) throws RemoteException {
		connectedClients.remove(cl);
		cl.removeConnectedTopic(this);
	}
	private void notifyMembers(String msg) throws RemoteException{
		for(IClient c : connectedClients){
			c.refresh(msg);
		}
	}
}
