package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Account extends UnicastRemoteObject implements IAccount {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3768006366498340145L;
	private String pseudo;
	private ArrayList<String> subscription_list;
	
	public Account(String p) throws RemoteException{
		this.pseudo = p;
		subscription_list = new ArrayList<String>();
	}
	
	public void addSubscription(String topictitle) throws RemoteException{
		subscription_list.add(topictitle);
		//ENREGISTRER DANS FICHIER
	}
	public void removeSubscription(String topictitle) throws RemoteException{
		subscription_list.remove(topictitle);
	}
	
	public String getPseudo() throws RemoteException{
		return pseudo;
	}
	public void setPseudo(String pseudo) throws RemoteException{
		this.pseudo = pseudo;
	}
	public ArrayList<String> getSubscription_list() throws RemoteException {
		return subscription_list;
	}
	public void setSubscription_list(ArrayList<String> subscription_list) throws RemoteException {
		this.subscription_list = subscription_list;
	}
}
