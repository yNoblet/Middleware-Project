package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * The Class Account.
 */
public class Account extends UnicastRemoteObject implements IAccount {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3768006366498340145L;
	
	/** The pseudo of the account. */
	private String pseudo;
	
	/** The number of messages written per account. */
	private int nbMsg;
	
	/** The subscription list contains subscribed topics titles. */
	private ArrayList<String> subscriptionList;

	/**
	 * Constructor.
	 *
	 * @param pseudo the pseudo of the account
	 * @throws RemoteException the remote exception
	 */
	public Account(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
		this.subscriptionList = new ArrayList<String>();
	}
	
	/**
	 * @see core.IAccount#getPseudo()
	 */
	@Override
	public String getPseudo() throws RemoteException {
		return this.pseudo;
	}

	/**
	 * @see core.IAccount#getNbMsg()
	 */
	@Override
	public int getNbMsg() {
		return this.nbMsg;
	}

	/**
	 * @see core.IAccount#addNbMsg(int)
	 */
	@Override
	public void incrementsNbMsg() {
		this.nbMsg++;
	}

	/**
	 * @see core.IAccount#addSubscription(java.lang.String)
	 */
	@Override
	public void addSubscription(String topictitle) throws RemoteException {
		this.subscriptionList.add(topictitle);
		//TODO ENREGISTRER DANS FICHIER
	}

	/**
	 * @see core.IAccount#removeSubscription(java.lang.String)
	 */
	@Override
	public void removeSubscription(String topictitle) throws RemoteException {
		this.subscriptionList.remove(topictitle);
	}

	/**
	 * @see core.IAccount#getSubscriptionList()
	 */
	@Override
	public ArrayList<String> getSubscriptionList() throws RemoteException {
		return this.subscriptionList;
	}
}
