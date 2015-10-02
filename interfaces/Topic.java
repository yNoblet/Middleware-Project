package interfaces;

import java.util.ArrayList;

public class Topic {
	private String id;
	private String title;
	private ArrayList<Client> client_list;
	private ArrayList<Message> historic;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<Client> getClient_list() {
		return client_list;
	}
	public void setClient_list(ArrayList<Client> client_list) {
		this.client_list = client_list;
	}
	public ArrayList<Message> getHistoric() {
		return historic;
	}
	public void setHistoric(ArrayList<Message> historic) {
		this.historic = historic;
	}
}
