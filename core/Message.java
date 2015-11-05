package core;

/**
 * The Class Message.
 */
public class Message {
	
	/** The author of the message. */
	private String author;
	
	/** The text of the message. */
	private String text;
	
	/**
	 * Instantiates a new message.
	 *
	 * @param pseudo the pseudo of the author
	 * @param message the content of the message
	 */
	public Message(String pseudo, String message) {
		this.author = pseudo;
		this.text = message;
	} 
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.author+": "+this.text+"\n";
	}

	/**
	 * Gets the author of the message.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return this.author;
	}
}
