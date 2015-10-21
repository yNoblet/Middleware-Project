package core;

public class Message {
	private String author;
	private String text;
	
	public Message(String pseudo, String message) {
		this.author = pseudo;
		this.text = message;
	} 
	
	@Override
	public String toString() {
		return this.author+": "+this.text+"\n";
	}

	public String getAuthor() {
		return this.author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getText() {
		return this.text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
