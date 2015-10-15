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

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public Map<String, Integer> getClientList() {
		return clientList;
	}

	public void setClientList(Map<String, Integer> client_list) {
		clientList = client_list;
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
		// clientList = new HashSet<String>();
		clientList = new HashMap<String, Integer>();
		clientList.put(author, 0);
		historic = new ArrayList<Message>();
		connectedClients = new HashSet<IClient>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
		Date d = new Date();
		date = dateFormat.format(d);
	}

	public String getHistoricString() {
		String hist = "";
		for (Message msg : historic) {
			hist += msg.toString();
		}
		return hist;
	}

	@Override
	public void subscribe(String pseudo) throws RemoteException {
		clientList.put(pseudo, 0);
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

	private void notifyMembers(String msg) throws RemoteException {
		for (IClient c : connectedClients) {
			c.refresh(msg);
		}
	}

	@Override
	public void addNbMsg(String client) throws RemoteException {
		getClientList().replace(client, getClientList().get(client) + 1);
	}

	@Override
	public String getDate() throws RemoteException {
		return date;
	}
}
