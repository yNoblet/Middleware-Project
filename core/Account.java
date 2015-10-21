package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Account extends UnicastRemoteObject implements IAccount {
	/**
	 *
	 */
	private static final long serialVersionUID = 3768006366498340145L;
	private String pseudo;
	private int nbMsg;
	private ArrayList<String> subscriptionList;

	public Account(String p) throws RemoteException {
		this.pseudo = p;
		this.subscriptionList = new ArrayList<String>();
	}

	@Override
	public void addSubscription(String topictitle) throws RemoteException {
		this.subscriptionList.add(topictitle);
		// ENREGISTRER DANS FICHIER
	}

	@Override
	public void removeSubscription(String topictitle) throws RemoteException {
		this.subscriptionList.remove(topictitle);
	}

	@Override
	public ArrayList<String> getSubscriptionList() throws RemoteException {
		return this.subscriptionList;
	}
	
	@Override
	public String getPseudo() throws RemoteException {
		return this.pseudo;
	}

	@Override
	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
	public int getNbMsg() {
		return this.nbMsg;
	}

	@Override
	public void addNbMsg(int nbMsg) {
		this.nbMsg++;
	}
}
