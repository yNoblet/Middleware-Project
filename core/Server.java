package core;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Server extends UnicastRemoteObject implements IServer {
	/**
	 *
	 */
	private static final long serialVersionUID = 5348901788833610650L;
	private Map<String, IAccount> accounts;
	private Map<String, ITopic> topics;
	private Collection<IClient> connectedClient;
	private Collection<IServer> otherServers;

	public Server(String adrP, String portP, String portS) throws RemoteException, NotBoundException, AlreadyBoundException {
		this.accounts = new HashMap<String, IAccount>();
		this.topics = new HashMap<String, ITopic>();
		this.connectedClient = new ArrayList<IClient>();
		this.otherServers = new ArrayList<IServer>();
		Registry reg = java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(portS));
		reg.bind("Server", this);
		
		if (adrP.equals("") || portP.equals("")) {
			System.out.println("first server");
		} else {
			int remotePort = Integer.parseInt(portP);
			String remoteIp = adrP;
			String remoteObjectName = "Server";
			Registry registryMain = LocateRegistry.getRegistry(remoteIp, remotePort);
			IServer s = (IServer) registryMain.lookup(remoteObjectName);
			s.addServer(this);
			// s.addServer(adrP, Integer.parseInt(portP));
			// addServer(adrP, Integer.parseInt(portP));
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					onDisconnect();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
				}
			}
		}));
	}
	
	public IServer checkPartition() throws RemoteException{
		IServer s = this;
		int nbConnectedClient = this.getConnectedClient().size();
		for (IServer serv : this.otherServers) {
			if(serv.getConnectedClient().size() < nbConnectedClient){
				s = serv;
				nbConnectedClient = serv.getConnectedClient().size();
			}
		}
		return s;
	}

	public void onDisconnect() throws RemoteException {
		for (IClient c : this.connectedClient) {
			try {
				c.serverDown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized boolean newTopic(String title, String p) throws RemoteException {
		if (this.topics.get(title) == null) {
			this.addTopic(title, new Topic(title, p));
			this.accounts.get(p).addSubscription(title);
			for (IClient c : this.connectedClient) {
				if (!c.getPseudo().equals(p)) {
					c.addTopic(title);
				}
			}
			//notify other servers
			for (IServer serv : this.otherServers) {
				serv.addTopic(title, new Topic(title, p));
				//serv.getAccounts().get(p).addSubscription(title);
				serv.setAccountList(this.accounts);
				for (IClient c : serv.getConnectedClient()) {
					if (!c.getPseudo().equals(p)) {
						c.addTopic(title);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public synchronized boolean deleteTopic(String title) throws RemoteException {
		Topic t = (Topic) this.topics.get(title);
		for (Entry<String, Integer> entry : t.getClientList().entrySet()) {
			this.accounts.get(entry.getKey()).removeSubscription(title);
		}

		this.topics.remove(title);
		for (IClient c : this.connectedClient) {
			c.removeTopic(title);
		}
		
		//notify other servers
		for (IServer serv : this.otherServers) {
			/*for (Entry<String, Integer> entry : t.getClientList().entrySet()) {
				serv.getAccounts().get(entry.getKey()).removeSubscription(title);
			}*/
			serv.setAccountList(this.accounts);

			serv.getTopicList().remove(title);
			for (IClient c : serv.getConnectedClient()) {
				c.removeTopic(title);
			}
		}
		return true;
	}
	
	@Override
	public void addTopic(String title, ITopic topic) throws RemoteException {
		this.topics.put(title, topic);
	}
	
	@Override
	public synchronized ITopic getTopic(String title) throws RemoteException {
		return this.topics.get(title);
	}
	
	public Map<String, ITopic> getTopicList() {
		return this.topics;
	}

	public void setTopicList(Map<String, ITopic> topicList) {
		this.topics = topicList;
	}
	
	@Override
	public synchronized Collection<String> getTopicTitles() throws RemoteException {
		ArrayList<String> l = new ArrayList<String>();
		l.addAll(this.topics.keySet());
		return l;
	}

	@Override
	public synchronized void addClient(IClient cl) throws RemoteException {
		this.connectedClient.add(cl);
	}

	@Override
	public synchronized void removeClient(IClient cl) throws RemoteException {
		this.connectedClient.remove(cl);
	}
	
	@Override
	public synchronized void addServer(IServer s) throws RemoteException {
		this.otherServers.add(s);
		System.out.println("main "+this.toString());
		for (IServer serv : this.otherServers) {
			serv.setOtherServers(this.otherServers, this);
		}
	}

	public synchronized void removeServer(Server s) throws RemoteException {
		this.otherServers.remove(s);
		for (IServer serv : this.otherServers) {
			serv.setOtherServers(this.otherServers, this);
		}
	}
	
	@Override
	public void setOtherServers(Collection<IServer> listServers, IServer me) {
		this.otherServers = listServers;
		this.otherServers.add(me);
		System.out.println("sec "+this.toString());
	}

	@Override
	public Collection<IClient> getConnectedClient() throws RemoteException {
		return this.connectedClient;
	}
	
	@Override
	public synchronized IAccount getAccount(String cl) throws RemoteException {
		if (this.accounts.get(cl) == null) {
			this.accounts.put(cl, new Account(cl));
			for (IServer serv : this.otherServers) {
				serv.setAccountList(this.accounts);
			}
		}
		return this.accounts.get(cl);
	}
	
	@Override
	public Map<String, IAccount> getAccountList() throws RemoteException {
		return this.accounts;
	}

	public void setAccountList(Map<String, IAccount> accs) throws RemoteException {
		this.accounts = accs;
	}

}
