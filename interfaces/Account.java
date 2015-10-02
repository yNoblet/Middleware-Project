package interfaceMiddleware;

import java.util.ArrayList;

public class Account {
	private String pseudo;
	private ArrayList<String> subscription_list;
	
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public ArrayList<String> getSubscription_list() {
		return subscription_list;
	}
	public void setSubscription_list(ArrayList<String> subscription_list) {
		this.subscription_list = subscription_list;
	}
}
