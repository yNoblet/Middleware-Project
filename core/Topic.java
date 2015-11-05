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

/**
 * The Class Topic.
 */
public class Topic extends UnicastRemoteObject implements ITopic {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3180799191650643596L;
	
	/** The title of the topic. */
	private String title;
	
	/** The author of the topic. */
	private String author;
	
	/** The list of clients who subscribed. */
	private Map<String, Integer> clientList;
	
	/** The historic of messages. */
	private ArrayList<Message> historic;
	
	/** Currently connected clients. */
	private Set<IClient> connectedClients;
	
	/** The creation date. */
	private String date;
	
	/**
	 * Instantiates a new topic.
	 *
	 * @param title the title
	 * @param author the author
	 * @throws RemoteException the remote exception
	 */
	public Topic(String title, String author) throws RemoteException {
		super();
		this.title = title;
		this.author = author;
		this.clientList = new HashMap<String, Integer>();
		this.clientList.put(author, 0);
		this.historic = new ArrayList<Message>();
		this.connectedClients = new HashSet<IClient>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
		Date d = new Date();
		this.date = dateFormat.format(d);
	}
	
	/**
	 * @see core.ITopic#getTitle()
	 */
	@Override
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * @see core.ITopic#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return this.author;
	}
	
	/**
	 * @see core.ITopic#getDate()
	 */
	@Override
	public String getDate() throws RemoteException {
		return this.date;
	}
	
	/**
	 * Gets the historic of the topic.
	 *
	 * @return the historic
	 */
	public ArrayList<Message> getHistoric() {
		return this.historic;
	}

	/**
	 * Gets the historic as a string object.
	 *
	 * @return the historic string
	 */
	public String getHistoricString() {
		String hist = "";
		for (Message msg : this.historic) {
			hist += msg.toString();
		}
		return hist;
	}
	
	/**
	 * Sets the historic of the topic.
	 *
	 * @param historic the new historic
	 */
	public void setHistoric(ArrayList<Message> historic) {
		this.historic = historic;
	}
	
	/**
	 * @see core.ITopic#getClientList()
	 */
	@Override
	public Map<String, Integer> getClientList() {
		return this.clientList;
	}
	
	/**
	 * @see core.ITopic#post(java.lang.String, java.lang.String)
	 */
	@Override
	public void post(String pseudo, String message) throws RemoteException {
		Message msg = new Message(pseudo, message);
		this.historic.add(msg);
		notifyMembers(msg.toString());
	}
	
	/**
	 * Notify members of the topic with the new message.
	 *
	 * @param msg the message
	 * @throws RemoteException the remote exception
	 */
	private void notifyMembers(String msg) throws RemoteException {
		ArrayList<IClient> clientToRemove = new ArrayList<>();
		for (IClient c : this.connectedClients) {
			try{
				c.refresh(msg);
			}catch(RemoteException e){
				System.out.println("client deconnecté");
				clientToRemove.add(c);
			}
		}
		connectedClients.removeAll(clientToRemove);
	}
	
	/**
	 * @see core.ITopic#addNbMsg(java.lang.String)
	 */
	@Override
	public void refreshNbMsg(String client) throws RemoteException {
		getClientList().replace(client, getClientList().get(client) + 1);
	}
	
	/**
	 * @see core.ITopic#subscribe(java.lang.String)
	 */
	@Override
	public void subscribe(String pseudo) throws RemoteException {
		this.clientList.put(pseudo, 0);
	}

	/**
	 * @see core.ITopic#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String pseudo) throws RemoteException {
		this.clientList.remove(pseudo);
	}

	/**
	 * @see core.ITopic#connectClient(core.IClient)
	 */
	@Override
	public void connectClient(IClient cl) throws RemoteException {
		this.connectedClients.add(cl);
		cl.setConnectedTopic(this);
		cl.refresh(getHistoricString());
	}

	/**
	 * @see core.ITopic#disconnectClient(core.IClient)
	 */
	@Override
	public void disconnectClient(IClient cl) throws RemoteException {
		this.connectedClients.remove(cl);
		cl.removeConnectedTopic();
	}

}
