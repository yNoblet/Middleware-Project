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

/**
 * The Class Server.
 */
public class Server extends UnicastRemoteObject implements IServer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5348901788833610650L;
	
	/** The list of accounts. */
	private Map<String, IAccount> accounts;
	
	/** The list of topics. */
	private Map<String, ITopic> topics;
	
	/** The list of connected clients. */
	private Collection<IClient> connectedClients;
	
	/** The list of servers. */
	private Collection<IServer> listServers;
	
	/** The IP address of the server. */
	private String ip;
	
	/** The port of connection of the server. */
	private String port;

	/**
	 * Instantiates a new server.
	 *
	 * @param addrMain the IP address of an online server
	 * @param portMain the port of an online server
	 * @param portSelf the port for this server
	 * @throws RemoteException the remote exception
	 * @throws NotBoundException the not bound exception
	 * @throws AlreadyBoundException the already bound exception
	 * @throws UnknownHostException the unknown host exception
	 */
	public Server(String addrMain, String portMain, String portSelf) throws RemoteException, NotBoundException, AlreadyBoundException, UnknownHostException {
		Registry reg = java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(portSelf));
		reg.bind("Server", this);
		this.ip = InetAddress.getLocalHost().getHostAddress();
		this.port = portSelf;
		
		this.accounts = new HashMap<String, IAccount>();
		this.topics = new HashMap<String, ITopic>();
		this.connectedClients = new ArrayList<IClient>();
		this.listServers = new ArrayList<IServer>();
		
		if (addrMain.equals("") || portMain.equals("")) {
			this.listServers.add(this);
			System.out.println("first server");
		} else {
			int remotePort = Integer.parseInt(portMain);
			String remoteIp = addrMain;
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
	
	/**
	 * @see core.IServer#getPort()
	 */
	@Override
	public String getPort() throws RemoteException {
		return this.port;
	}

	/**
	 * @see core.IServer#getIP()
	 */
	@Override
	public String getIP() throws RemoteException {
		return this.ip;
	}
	
	/**
	 * @see core.IServer#checkDistribution()
	 */
	public IServer checkDistribution() throws RemoteException{
		IServer s = this;
		int nbConnectedClient = this.getConnectedClients().size();
		ArrayList<IServer> serverToRemove = new ArrayList<>();
		for (IServer serv : this.listServers) {
			try{
				if(serv.getConnectedClients().size() < nbConnectedClient){
					s = serv;
					nbConnectedClient = serv.getConnectedClients().size();
				}
			}catch(RemoteException e){
				System.out.println("Exception : (Server) checkDistribution : server offline");
				serverToRemove.add(serv);
			}
		}
		listServers.removeAll(serverToRemove);
		return s;
	}
	
	/**
	 * @see core.IServer#addServer(core.IServer)
	 */
	@Override
	public synchronized void addServer(IServer s) throws RemoteException, NotBoundException {
		this.listServers.add(s);
	}

	/**
	 * @see core.IServer#removeServer(core.IServer)
	 */
	public synchronized void removeServer(IServer s) throws RemoteException {
		this.listServers.remove(s);
	}
	
	/**
	 * @see core.IServer#getServerList()
	 */
	@Override
	public Collection<IServer> getServerList() throws RemoteException {
		return this.listServers;
	}
	
	/**
	 * @see core.IServer#postMessage(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void postMessage(String topicTitle, String author, String msg) throws RemoteException {
		IServer s = this;
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<IServer> serverToRemove = new ArrayList<>();
				for (IServer serv : listServers) {
					if(!serv.equals(s)){
						try{
							System.out.println(serv.getPort());
							serv.getTopic(topicTitle).post(author, msg);
						}catch(RemoteException e){
							System.out.println("Exception : (Server) postMessage : server offline");
							serverToRemove.add(serv);
						}
					}
				}
				listServers.removeAll(serverToRemove);
			}
			
		});
		t.start();
		this.getTopic(topicTitle).post(author, msg);
	}

	/**
	 * @see core.IServer#newTopic(java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized boolean onCreateNewTopic(String title, String author) throws RemoteException {
		IServer s = this;
		if (this.topics.get(title) == null) {
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					ArrayList<IServer> serverToRemove = new ArrayList<>();
					ArrayList<IClient> clientToRemove = new ArrayList<>();
					//notify all servers
					for (IServer serv : listServers) {
						if(!serv.equals(s)){
							try {
								serv.addTopic(title, author);
								serv.getAccount(author).addSubscription(title);
								
								for (IClient c : serv.getConnectedClients()) {
									try {
										if (!c.getPseudo().equals(author)) {
											c.onCreateTopic(title);
										}
									}catch(RemoteException e){
										System.out.println("Exception : (Server) onCreateNewTopic : client offline");
										clientToRemove.add(c);
									}
								}
							} catch (RemoteException e) {
								System.out.println("Exception : (Server) onCreateNewTopic : server offline");
								serverToRemove.add(serv);
								//e.printStackTrace();
							}
						}
					}
					listServers.removeAll(serverToRemove);
					connectedClients.removeAll(clientToRemove);
				}
			});
			t.start();
			this.addTopic(title, author);
			this.getAccount(author).addSubscription(title);
			
			for (IClient c : this.getConnectedClients()) {
				if (!c.getPseudo().equals(author)) {
					c.onCreateTopic(title);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @see core.IServer#deleteTopic(java.lang.String)
	 */
	@Override
	public synchronized boolean onDeleteTopic(String title) throws RemoteException {
		IServer s = this;
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<IServer> serverToRemove = new ArrayList<>();
				ArrayList<IClient> clientToRemove = new ArrayList<>();
				//notify all servers
				for (IServer serv : listServers) {
					if(!serv.equals(s)){
						try {
							ITopic top = serv.getTopic(title);
							for (Entry<String, Integer> entry : top.getClientList().entrySet()) {
								serv.getAccountList().get(entry.getKey()).removeSubscription(title);
							}
							serv.removeTopic(title);
							
							for (IClient c : serv.getConnectedClients()) {
								try {
									c.onDeleteTopic(title);
								}catch(RemoteException e){
									System.out.println("Exception : (Server) onDeleteTopic : client offline");
									clientToRemove.add(c);
								}
							}
						} catch (RemoteException e) {
							System.out.println("Exception : (Server) onDeleteTopic : server offline");
							serverToRemove.add(serv);
							//e.printStackTrace();
						}
					}
				}
				listServers.removeAll(serverToRemove);
				connectedClients.removeAll(clientToRemove);
			}
		});
		t.start();

		for (Entry<String, Integer> entry : this.getTopic(title).getClientList().entrySet()) {
			this.getAccountList().get(entry.getKey()).removeSubscription(title);
		}
		this.getTopicList().remove(title);
		
		for (IClient c : this.getConnectedClients()) {
			c.onDeleteTopic(title);
		}
		return true;
	}
	
	/**
	 * @see core.IServer#addTopic(java.lang.String, java.lang.String)
	 */
	@Override
	public void addTopic(String title, String author) throws RemoteException {
		this.topics.put(title, new Topic(title, author));
	}
	
	/**
	 * @see core.IServer#removeTopic(java.lang.String)
	 */
	@Override
	public void removeTopic(String topicTitle) throws RemoteException {
		this.topics.remove(topicTitle);
	}
	
	/**
	 * @see core.IServer#getTopic(java.lang.String)
	 */
	@Override
	public synchronized ITopic getTopic(String topicTitle) throws RemoteException {
		return this.topics.get(topicTitle);
	}
	
	/**
	 * @see core.IServer#getTopicList()
	 */
	public Map<String, ITopic> getTopicList() {
		return this.topics;
	}
	
	/**
	 * @see core.IServer#getTopicTitles()
	 */
	@Override
	public synchronized Collection<String> getTopicTitles() throws RemoteException {
		ArrayList<String> l = new ArrayList<String>();
		l.addAll(this.topics.keySet());
		return l;
	}

	/**
	 * @see core.IServer#addClient(core.IClient)
	 */
	@Override
	public synchronized void addClient(IClient client) throws RemoteException {
		this.connectedClients.add(client);
	}

	/**
	 * @see core.IServer#removeClient(core.IClient)
	 */
	@Override
	public synchronized void removeClient(IClient client) throws RemoteException {
		this.connectedClients.remove(client);
	}
	
	/**
	 * @see core.IServer#getConnectedClient()
	 */
	@Override
	public Collection<IClient> getConnectedClients() throws RemoteException {
		return this.connectedClients;
	}
	
	/**
	 * @see core.IServer#addAccount(java.lang.String)
	 */
	@Override
	public void createAccount(String client) throws RemoteException {
		this.accounts.put(client, new Account(client));
	}
	
	/**
	 * @see core.IServer#removeAccount(java.lang.String)
	 */
	@Override
	public void removeAccount(String pseudo) throws RemoteException {
		this.accounts.remove(pseudo);
	}
	
	/**
	 * @see core.IServer#getAccount(java.lang.String)
	 */
	@Override
	public synchronized IAccount getAccount(String pseudo) throws RemoteException {
		IServer s = this;
		if (this.accounts.get(pseudo) == null) {
			Thread t = new Thread(new Runnable(){
				@Override
				public void run() {
					ArrayList<IServer> serverToRemove = new ArrayList<>();
					for (IServer serv : listServers) {
						if(!serv.equals(s))
							try {
								serv.createAccount(pseudo);
							} catch (RemoteException e) {
								System.out.println("Exception : (Server) getAccount : server offline");
								serverToRemove.add(serv);
								//e.printStackTrace();
							}
					}
					listServers.removeAll(serverToRemove);
				}
			});
			t.start();
			this.createAccount(pseudo);
		}
		return this.accounts.get(pseudo);
	}
	
	/**
	 * @see core.IServer#getAccountList()
	 */
	@Override
	public Map<String, IAccount> getAccountList() throws RemoteException {
		return this.accounts;
	}
	
	/**
	 * @see core.IServer#subscribe(java.lang.String, java.lang.String)
	 */
	@Override
	public void subscribe(String topicTitle, String pseudo) throws RemoteException {
		IServer s = this;
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<IServer> serverToRemove = new ArrayList<>();
				for(IServer serv : listServers){
					if(!serv.equals(s)){
						try {
							serv.getTopic(topicTitle).subscribe(pseudo);
							serv.getAccount(pseudo).addSubscription(topicTitle);
						} catch (RemoteException e) {
							System.out.println("Exception : (Server) subscribe : server offline");
							serverToRemove.add(serv);
							//e.printStackTrace();
						}
					}
				}
				listServers.removeAll(serverToRemove);
			}
			
		});
		t.start();
		this.getTopic(topicTitle).subscribe(pseudo);
		this.getAccount(pseudo).addSubscription(topicTitle);
	}
	
	/**
	 * @see core.IServer#unsubscribe(java.lang.String, java.lang.String)
	 */
	@Override
	public void unsubscribe(String topicTitle, String pseudo) throws RemoteException {
		IServer s = this;
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<IServer> serverToRemove = new ArrayList<>();
				for(IServer serv : listServers){
					if(!serv.equals(s)){
						try {
							serv.getTopic(topicTitle).unsubscribe(pseudo);
							serv.getAccount(pseudo).removeSubscription(topicTitle);
						} catch (RemoteException e) {
							System.out.println("Exception : (Server) unsubscribe : server offline");
							serverToRemove.add(serv);
							//e.printStackTrace();
						}
					}
				}
				listServers.removeAll(serverToRemove);
			}
			
		});
		t.start();
		this.getTopic(topicTitle).unsubscribe(pseudo);
		this.getAccount(pseudo).removeSubscription(topicTitle);
	}
	
	/**
	 * Handles server activity on disconnect.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void onDisconnect() throws RemoteException {
		for (IClient c : this.connectedClients) {
			try {
				c.onServerDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
