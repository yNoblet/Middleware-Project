package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Topic extends UnicastRemoteObject implements ITopic {

	/**
	 *
	 */
	private static final long serialVersionUID = -3180799191650643596L;
	private String title;
	private String author;
	// private Set<String> clientList;
	private Map<String, Integer> clientList;
	private ArrayList<Message> historic;
	private Set<IClient> connectedClients;
	private String date;
	
	public Topic(String title, String author) throws RemoteException {
		super();
		this.title = title;
		this.author = author;
		// clientList = new HashSet<String>();
		this.clientList = new HashMap<String, Integer>();
		this.clientList.put(author, 0);
		this.historic = new ArrayList<Message>();
		this.connectedClients = new HashSet<IClient>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
		Date d = new Date();
		this.date = dateFormat.format(d);
	}
	
	@Override
	public void subscribe(String pseudo) throws RemoteException {
		this.clientList.put(pseudo, 0);
	}

	@Override
	public void unsubscribe(String pseudo) throws RemoteException {
		this.clientList.remove(pseudo);
	}

	@Override
	public void post(String pseudo, String message) throws RemoteException {
		Message msg = new Message(pseudo, message);
		this.historic.add(msg);
		notifyMembers(msg.toString());
	}

	@Override
	public void connectClient(IClient cl) throws RemoteException {
		this.connectedClients.add(cl);
		cl.addConnectedTopic(this);
		cl.refresh(getHistoricString());
	}

	@Override
	public void disconnectClient(IClient cl) throws RemoteException {
		this.connectedClients.remove(cl);
		cl.removeConnectedTopic(this.getTitle());
	}

	private void notifyMembers(String msg) throws RemoteException {
		for (IClient c : this.connectedClients) {
			c.refresh(msg);
		}
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public Map<String, Integer> getClientList() {
		return this.clientList;
	}

	public void setClientList(Map<String, Integer> clientList) {
		this.clientList = clientList;
	}

	public ArrayList<Message> getHistoric() {
		return this.historic;
	}

	public void setHistoric(ArrayList<Message> historic) {
		this.historic = historic;
	}

	public String getHistoricString() {
		String hist = "";
		for (Message msg : this.historic) {
			hist += msg.toString();
		}
		return hist;
	}

	@Override
	public void addNbMsg(String client) throws RemoteException {
		getClientList().replace(client, getClientList().get(client) + 1);
	}

	@Override
	public String getDate() throws RemoteException {
		return this.date;
	}
}
