package core;

public class Message {
	private String author;
	private String text;
	
	public Message(String pseudo, String message) {
		author = pseudo;
		text = message;
	} 
	
	@Override
	public String toString() {
		return author+": "+text+"\n";
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
