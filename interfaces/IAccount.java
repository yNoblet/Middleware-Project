package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IAccount extends Remote{
	public void addSubscription(String topictitle) throws RemoteException;
	public void removeSubscription(String topictitle) throws RemoteException;
	public String getPseudo() throws RemoteException;
	public void setPseudo(String pseudo) throws RemoteException;
	public ArrayList<String> getSubscription_list() throws RemoteException;
	public void setSubscription_list(ArrayList<String> subscription_list) throws RemoteException ;
}
