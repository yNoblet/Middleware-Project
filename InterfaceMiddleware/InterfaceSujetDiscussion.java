package InterfaceMiddleware;

import java.rmi.*;

public interface InterfaceSujetDiscussion extends Remote {
public void inscription(InterfaceAffichageClient c)
                          throws RemoteException;
public void desInscription(InterfaceAffichageClient c)
                          throws RemoteException;
public void diffuse(String Message)
                          throws RemoteException;
}
