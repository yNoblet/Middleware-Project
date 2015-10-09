package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IAccount extends Remote{
	public void addSubscription(String topicTitle) throws RemoteException;
	public void removeSubscription(String topicTitle) throws RemoteException;
	public String getPseudo() throws RemoteException;
	public void setPseudo(String pseudo) throws RemoteException;
	public ArrayList<String> getSubscriptionList() throws RemoteException;
}
