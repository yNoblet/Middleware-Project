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

// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public class Server extends UnicastRemoteObject implements IServer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5348901788833610650L;
	
	/** The accounts. */
	private Map<String, IAccount> accounts;
	
	/** The topics. */
	private Map<String, ITopic> topics;
	
	/** The connected client. */
	private Collection<IClient> connectedClient;
	
	/** The list servers. */
	private Collection<IServer> listServers;
	
	/** The ip. */
	private String ip;
	
	/** The port. */
	private String port;

	/**
	 * Instantiates a new server.
	 *
	 * @param adrP the adr p
	 * @param portP the port p
	 * @param portS the port s
	 * @throws RemoteException the remote exception
	 * @throws NotBoundException the not bound exception
	 * @throws AlreadyBoundException the already bound exception
	 * @throws UnknownHostException the unknown host exception
	 */
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
	
	/* (non-Javadoc)
	 * @see core.IServer#checkPartition()
	 */
	public IServer checkPartition() throws RemoteException{
		IServer s = this;
		int nbConnectedClient = this.getConnectedClient().size();
		ArrayList<IServer> serverToRemove = new ArrayList<>();
		for (IServer serv : this.listServers) {
			try{
				if(serv.getConnectedClient().size() < nbConnectedClient){
					s = serv;
					nbConnectedClient = serv.getConnectedClient().size();
				}
			}catch(RemoteException e){
				System.out.println("Exception : (Server)checkPartition : server offline");
				serverToRemove.add(serv);
			}
		}
		listServers.removeAll(serverToRemove);
		return s;
	}

	/**
	 * On disconnect.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void onDisconnect() throws RemoteException {
		for (IClient c : this.connectedClient) {
			try {
				c.onServerDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.IServer#newTopic(java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized boolean newTopic(String title, String author) throws RemoteException {
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
								
								for (IClient c : serv.getConnectedClient()) {
									try {
										if (!c.getPseudo().equals(author)) {
											c.onCreateTopic(title);
										}
									}catch(RemoteException e){
										System.out.println("Exception : (Server) newTopic : client offline");
										clientToRemove.add(c);
									}
								}
							} catch (RemoteException e) {
								System.out.println("Exception : (Server) newTopic : server offline");
								serverToRemove.add(serv);
								//e.printStackTrace();
							}
						}
					}
					listServers.removeAll(serverToRemove);
					connectedClient.removeAll(clientToRemove);
				}
			});
			t.start();
			this.addTopic(title, author);
			this.getAccount(author).addSubscription(title);
			
			for (IClient c : this.getConnectedClient()) {
				if (!c.getPseudo().equals(author)) {
					c.onCreateTopic(title);
				}
			}
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#deleteTopic(java.lang.String)
	 */
	@Override
	public synchronized boolean deleteTopic(String title) throws RemoteException {
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
							
							for (IClient c : serv.getConnectedClient()) {
								try {
									c.onDeleteTopic(title);
								}catch(RemoteException e){
									System.out.println("Exception : (Server) deleteTopic : client offline");
									clientToRemove.add(c);
								}
							}
						} catch (RemoteException e) {
							System.out.println("Exception : (Server) deleteTopic : server offline");
							serverToRemove.add(serv);
							//e.printStackTrace();
						}
					}
				}
				listServers.removeAll(serverToRemove);
				connectedClient.removeAll(clientToRemove);
			}
		});
		t.start();

		for (Entry<String, Integer> entry : this.getTopic(title).getClientList().entrySet()) {
			this.getAccountList().get(entry.getKey()).removeSubscription(title);
		}
		this.getTopicList().remove(title);
		
		for (IClient c : this.getConnectedClient()) {
			c.onDeleteTopic(title);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#addTopic(java.lang.String, java.lang.String)
	 */
	@Override
	public void addTopic(String title, String author) throws RemoteException {
		this.topics.put(title, new Topic(title, author));
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#getTopic(java.lang.String)
	 */
	@Override
	public synchronized ITopic getTopic(String title) throws RemoteException {
		return this.topics.get(title);
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#getTopicList()
	 */
	public Map<String, ITopic> getTopicList() {
		return this.topics;
	}

	/**
	 * Sets the topic list.
	 *
	 * @param topicList the topic list
	 */
	public void setTopicList(Map<String, ITopic> topicList) {
		this.topics = topicList;
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#getTopicTitles()
	 */
	@Override
	public synchronized Collection<String> getTopicTitles() throws RemoteException {
		ArrayList<String> l = new ArrayList<String>();
		l.addAll(this.topics.keySet());
		return l;
	}

	/* (non-Javadoc)
	 * @see core.IServer#addClient(core.IClient)
	 */
	@Override
	public synchronized void addClient(IClient cl) throws RemoteException {
		this.connectedClient.add(cl);
	}

	/* (non-Javadoc)
	 * @see core.IServer#removeClient(core.IClient)
	 */
	@Override
	public synchronized void removeClient(IClient cl) throws RemoteException {
		this.connectedClient.remove(cl);
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#addServer(core.IServer)
	 */
	@Override
	public synchronized void addServer(IServer s) throws RemoteException, NotBoundException {
		this.listServers.add(s);
	}

	/* (non-Javadoc)
	 * @see core.IServer#removeServer(core.IServer)
	 */
	public synchronized void removeServer(IServer s) throws RemoteException {
		this.listServers.remove(s);
	}
	
	/**
	 * Notify remove.
	 *
	 * @param s the s
	 * @throws RemoteException the remote exception
	 */
	public synchronized void notifyRemove(IServer s) throws RemoteException {
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<IServer> serverToRemove = new ArrayList<>();
				for (IServer serv : listServers) {
					try {
						serv.removeServer(s);
					} catch (RemoteException e) {
						System.out.println("Exception : (Server) notifyRemove : server offline");
						serverToRemove.add(serv);
						//e.printStackTrace();
					}
				}
				listServers.removeAll(serverToRemove);
			}
			
		});
		t.start();
	}

	/* (non-Javadoc)
	 * @see core.IServer#getConnectedClient()
	 */
	@Override
	public Collection<IClient> getConnectedClient() throws RemoteException {
		return this.connectedClient;
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#getAccount(java.lang.String)
	 */
	@Override
	public synchronized IAccount getAccount(String cl) throws RemoteException {
		IServer s = this;
		if (this.accounts.get(cl) == null) {
			Thread t = new Thread(new Runnable(){
				@Override
				public void run() {
					ArrayList<IServer> serverToRemove = new ArrayList<>();
					for (IServer serv : listServers) {
						if(!serv.equals(s))
							try {
								serv.addAccount(cl);
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
			this.addAccount(cl);
		}
		return this.accounts.get(cl);
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#getAccountList()
	 */
	@Override
	public Map<String, IAccount> getAccountList() throws RemoteException {
		return this.accounts;
	}

	/* (non-Javadoc)
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
							System.out.println("Exception : (Server)postMessage : server offline");
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

	/* (non-Javadoc)
	 * @see core.IServer#getPort()
	 */
	@Override
	public String getPort() throws RemoteException {
		return this.port;
	}

	/* (non-Javadoc)
	 * @see core.IServer#getIP()
	 */
	@Override
	public String getIP() throws RemoteException {
		return this.ip;
	}

	/* (non-Javadoc)
	 * @see core.IServer#getServerList()
	 */
	@Override
	public Collection<IServer> getServerList() throws RemoteException {
		return this.listServers;
	}

	/* (non-Javadoc)
	 * @see core.IServer#addAccount(java.lang.String)
	 */
	@Override
	public void addAccount(String cl) throws RemoteException {
		this.accounts.put(cl, new Account(cl));
	}
	
	/* (non-Javadoc)
	 * @see core.IServer#removeAccount(java.lang.String)
	 */
	@Override
	public void removeAccount(String cl) throws RemoteException {
		this.accounts.remove(cl);
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see core.IServer#removeTopic(java.lang.String)
	 */
	@Override
	public void removeTopic(String topicTitle) throws RemoteException {
		this.topics.remove(topicTitle);
	}
}
