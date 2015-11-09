package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import gui.ChatWindow;
import gui.TopicWindow;

/**
 * The Class Client.
 */
public class Client extends UnicastRemoteObject implements IClient {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5697099462906475057L;
	
	/** The pseudo of the client. */
	private String pseudo;
	
	/** The current connected topic. */
	private ITopic connectedTopic;
	
	/** The current server hosting the client. */
	IServer server;
	
	/** The account associate to the client. */
	IAccount account;
	
	/** The chat window. */
	ChatWindow cw;
	
	/** The topic window. */
	TopicWindow tw;

	/**
	 * Instantiates a new client.
	 *
	 * @param p the pseudo of the client
	 * @param s the server hosting the client
	 * @throws RemoteException the remote exception
	 */
	public Client(String p, IServer s) throws RemoteException {
		this.pseudo = p;
		this.server = s;
		this.server.addClient(this);
		this.account = s.getAccount(p);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					onDisconnect();
				} catch (RemoteException e) {
					System.err.println("Exception : (Client) constructor : client offline");
				}
			}
		}));
	}
	
	/**
	 * @see core.IClient#getPseudo()
	 */
	@Override
	public String getPseudo() throws RemoteException {
		return this.pseudo;
	}
	
	/**
	 * @see core.IClient#post(java.lang.String, java.lang.String)
	 */
	@Override
	public void post(String msg, String topicTitle) throws RemoteException {
		this.server.postMessage(topicTitle, this.pseudo, msg);
	}
	
	/**
	 * @see core.IClient#refresh(java.lang.String)
	 */
	@Override
	public void refresh(String message) throws RemoteException {
		this.cw.displayMsg(message);
	}
	
	/**
	 * @see core.IClient#setConnectedTopic(core.ITopic)
	 */
	@Override
	public void setConnectedTopic(ITopic t) throws RemoteException {
		this.connectedTopic = t;
	}
	
	/**
	 * @see core.IClient#removeConnectedTopic()
	 */
	@Override
	public void removeConnectedTopic() throws RemoteException {
		this.connectedTopic = null;
	}

	/**
	 * @see core.IClient#addSubscribedTopic(java.lang.String)
	 */
	@Override
	public void addSubscribedTopic(String t) throws RemoteException {
		this.account.addSubscription(t);
	}

	/**
	 * @see core.IClient#removeSubscribedTopic(java.lang.String)
	 */
	@Override
	public void removeSubscribedTopic(String t) throws RemoteException {
		this.account.removeSubscription(t);
	}

	/**
	 * @see core.IClient#onCreateTopic(java.lang.String)
	 */
	@Override
	public void onCreateTopic(String t) throws RemoteException {
		if (this.tw != null) {
			this.tw.addAvailableTopic(t);
		}
	}

	/**
	 * @see core.IClient#onDeleteTopic(java.lang.String)
	 */
	@Override
	public void onDeleteTopic(String t) throws RemoteException {
		if (this.tw != null) {
			this.tw.removeTopic(t);
		}
		if (this.cw != null && this.cw.getTopic().equals(t)) {
			this.cw.onDeleteTopic();
		}
	}
	
	/**
	 * On disconnect, handles the client activity.
	 *
	 * @throws RemoteException the remote exception
	 */
	private void onDisconnect() throws RemoteException {
		connectedTopic.disconnectClient(this);
		this.server.removeClient(this);
	}

	/**
	 * @see core.IClient#onServerDown()
	 */
	@Override
	public void onServerDown() throws RemoteException {
		System.out.println("Server down => exit");
		if (this.cw != null) {
			this.cw.serverDown();
		}
		if (this.tw != null) {
			this.tw.serverDown();
		}
	}
	
	/**
	 * @see core.IClient#setChatWindow(gui.ChatWindow, java.lang.String)
	 */
	@Override
	public void setChatWindow(ChatWindow cw, String topicTitle) throws RemoteException {
		this.cw = cw;
		cw.setPseudo(this.pseudo);
		cw.setServer(this.server);
		cw.setClient(this);
		cw.setTopic(topicTitle);
		this.server.getTopic(topicTitle).connectClient(this);
	}

	/**
	 * @see core.IClient#setTopicWindow(gui.TopicWindow)
	 */
	@Override
	public void setTopicWindow(TopicWindow tw) throws RemoteException {
		this.tw = tw;
		tw.setPseudo(this.pseudo);
		tw.setClient(this);
		tw.setServer(this.server);
		tw.setSubscribedTopic(this.account.getSubscriptionList());
		Collection<String> at = this.server.getTopicTitles();
		at.removeAll(this.account.getSubscriptionList());
		tw.setAvailableTopic(at);
	}

}
