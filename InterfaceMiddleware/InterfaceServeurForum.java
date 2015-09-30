package interfaceMiddleware;

import java.rmi.*;

public interface InterfaceServeurForum extends Remote {
public InterfaceSujetDiscussion obtientSujet(String titre)
                             throws RemoteException;

public String obtientTitresDesSujets() throws RemoteException;

public void proposeSujet(String titre, InterfaceSujetDiscussion sujet)
                                             throws RemoteException;
}

