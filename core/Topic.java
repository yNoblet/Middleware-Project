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

// TODO: Auto-generated Javadoc
/**
 * The Class Topic.
 */
public class Topic extends UnicastRemoteObject implements ITopic {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3180799191650643596L;
	
	/** The title. */
	private String title;
	
	/** The author. */
	private String author;
	
	/** The client list. */
	// private Set<String> clientList;
	private Map<String, Integer> clientList;
	
	/** The historic. */
	private ArrayList<Message> historic;
	
	/** The connected clients. */
	private Set<IClient> connectedClients;
	
	/** The date. */
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
	
	/* (non-Javadoc)
	 * @see core.ITopic#subscribe(java.lang.String)
	 */
	@Override
	public void subscribe(String pseudo) throws RemoteException {
		this.clientList.put(pseudo, 0);
	}

	/* (non-Javadoc)
	 * @see core.ITopic#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String pseudo) throws RemoteException {
		this.clientList.remove(pseudo);
	}

	/* (non-Javadoc)
	 * @see core.ITopic#post(java.lang.String, java.lang.String)
	 */
	@Override
	public void post(String pseudo, String message) throws RemoteException {
		Message msg = new Message(pseudo, message);
		this.historic.add(msg);
		notifyMembers(msg.toString());
	}

	/* (non-Javadoc)
	 * @see core.ITopic#connectClient(core.IClient)
	 */
	@Override
	public void connectClient(IClient cl) throws RemoteException {
		this.connectedClients.add(cl);
		cl.setConnectedTopic(this);
		cl.refresh(getHistoricString());
	}

	/* (non-Javadoc)
	 * @see core.ITopic#disconnectClient(core.IClient)
	 */
	@Override
	public void disconnectClient(IClient cl) throws RemoteException {
		this.connectedClients.remove(cl);
		cl.removeConnectedTopic();
	}

	/**
	 * Notify members.
	 *
	 * @param msg the msg
	 * @throws RemoteException the remote exception
	 */
	private void notifyMembers(String msg) throws RemoteException {
		ArrayList<IClient> clientToRemove = new ArrayList<>();
		for (IClient c : this.connectedClients) {
			System.out.println(c.getPseudo());
			try{
				c.refresh(msg);
			}catch(RemoteException e){
				System.out.println("client deconnecté");
				clientToRemove.add(c);
			}
		}
		connectedClients.removeAll(clientToRemove);
	}

	/* (non-Javadoc)
	 * @see core.ITopic#getTitle()
	 */
	@Override
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see core.ITopic#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Sets the author.
	 *
	 * @param author the new author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/* (non-Javadoc)
	 * @see core.ITopic#getClientList()
	 */
	@Override
	public Map<String, Integer> getClientList() {
		return this.clientList;
	}

	/**
	 * Sets the client list.
	 *
	 * @param clientList the client list
	 */
	public void setClientList(Map<String, Integer> clientList) {
		this.clientList = clientList;
	}

	/**
	 * Gets the historic.
	 *
	 * @return the historic
	 */
	public ArrayList<Message> getHistoric() {
		return this.historic;
	}

	/**
	 * Sets the historic.
	 *
	 * @param historic the new historic
	 */
	public void setHistoric(ArrayList<Message> historic) {
		this.historic = historic;
	}

	/**
	 * Gets the historic string.
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

	/* (non-Javadoc)
	 * @see core.ITopic#addNbMsg(java.lang.String)
	 */
	@Override
	public void addNbMsg(String client) throws RemoteException {
		getClientList().replace(client, getClientList().get(client) + 1);
	}

	/* (non-Javadoc)
	 * @see core.ITopic#getDate()
	 */
	@Override
	public String getDate() throws RemoteException {
		return this.date;
	}
}
