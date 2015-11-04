package core;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
	private Collection<IServer> listServers;
	private String ip;
	private String port;

	public Server(String adrP, String portP, String portS) throws RemoteException, NotBoundException, AlreadyBoundException, UnknownHostException {
		Registry reg = java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(portS));
		reg.bind("Server", this);
		this.ip = InetAddress.getLocalHost().getHostAddress();
		this.port = portS;
		
		this.accounts = new HashMap<String, IAccount>();
		this.topics = new HashMap<String, ITopic>();
		this.connectedClient = new ArrayList<IClient>();
		this.listServers = new ArrayList<IServer>();
		
		if (adrP.equals("") || portP.equals("")) {
			this.listServers.add(this);
			System.out.println("first server");
		} else {
			int remotePort = Integer.parseInt(portP);
			String remoteIp = adrP;
			String remoteObjectName = "Server";
			Registry registryMain = LocateRegistry.getRegistry(remoteIp, remotePort);
			IServer s = (IServer) registryMain.lookup(remoteObjectName);
			
			for (IServer serv : s.getServerList()) {
				this.addServer(serv);
				if(!serv.equals(s)){
					serv.addServer(this);
				}
			}
			s.addServer(this);
			this.listServers.add(this);

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
		for (IServer serv : this.listServers) {
			try{
				if(serv.getConnectedClient().size() < nbConnectedClient){
					s = serv;
					nbConnectedClient = serv.getConnectedClient().size();
				}
			}catch(Exception e){
				System.out.println("(Server)checkPartition : serveur HS");
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
	public synchronized boolean newTopic(String title, String author) throws RemoteException {
		if (this.topics.get(title) == null) {
			//notify all servers
			for (IServer serv : this.listServers) {
				serv.addTopic(title, author);
				serv.getAccount(author).addSubscription(title);
				
				for (IClient c : serv.getConnectedClient()) {
					if (!c.getPseudo().equals(author)) {
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
		
		//notify all servers
		for (IServer serv : this.listServers) {
			Topic t = (Topic) serv.getTopic(title);
			for (Entry<String, Integer> entry : t.getClientList().entrySet()) {
				serv.getAccountList().get(entry.getKey()).removeSubscription(title);
			}

			serv.getTopicList().remove(title);
			for (IClient c : serv.getConnectedClient()) {
				c.removeTopic(title);
			}
		}
		return true;
	}
	
	@Override
	public void addTopic(String title, String author) throws RemoteException {
		this.topics.put(title, new Topic(title, author));
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
	public synchronized void addServer(IServer s) throws RemoteException, NotBoundException {
		this.listServers.add(s);
	}

	public synchronized void removeServer(IServer s) throws RemoteException {
		this.listServers.remove(s);
	}
	
	public synchronized void notifyRemove(IServer s) throws RemoteException {
		for (IServer serv : this.listServers) {
			serv.removeServer(s);
		}
	}

	@Override
	public Collection<IClient> getConnectedClient() throws RemoteException {
		return this.connectedClient;
	}
	
	@Override
	public synchronized IAccount getAccount(String cl) throws RemoteException {
		if (this.accounts.get(cl) == null) {
			for (IServer serv : this.listServers) {
				serv.addAccount(cl);
			}
		}
		return this.accounts.get(cl);
	}
	
	@Override
	public Map<String, IAccount> getAccountList() throws RemoteException {
		return this.accounts;
	}

	@Override
	public void postMessage(String topicTitle, String author, String msg) throws RemoteException {
		for (IServer serv : this.listServers) {
			try{
				serv.getTopic(topicTitle).post(author, msg);
			}catch(Exception e){
				System.out.println("(Server)postMessage : serveur HS");
			}
			
		}
	}

	@Override
	public String getPort() throws RemoteException {
		return this.port;
	}

	@Override
	public String getIP() throws RemoteException {
		return this.ip;
	}

	@Override
	public Collection<IServer> getServerList() throws RemoteException {
		return this.listServers;
	}

	@Override
	public void addAccount(String cl) throws RemoteException {
		this.accounts.put(cl, new Account(cl));
	}
	
	@Override
	public void removeAccount(String cl) throws RemoteException {
		this.accounts.remove(cl);
	}
}
