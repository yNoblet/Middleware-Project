package interfaces;

import java.util.ArrayList;

public class Server {
	private ArrayList<Account> account_list;
	private ArrayList<Topic> topic_list;
	
	
	
	
	public ArrayList<Account> getAccount_list() {
		return account_list;
	}
	public void setAccount_list(ArrayList<Account> account_list) {
		this.account_list = account_list;
	}
	public ArrayList<Topic> getTopic_list() {
		return topic_list;
	}
	public void setTopic_list(ArrayList<Topic> topic_list) {
		this.topic_list = topic_list;
	}
}
