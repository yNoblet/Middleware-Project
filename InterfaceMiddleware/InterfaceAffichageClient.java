package InterfaceMiddleware;

import java.rmi.*;

public interface InterfaceAffichageClient extends Remote {
public void affiche(String Message) throws RemoteException;

}