package mz.co.standardbank.e_biller.sms;

/**
 * @author - C816346 on 2020/05/13
 */
public class Sms {
	private String recipient;
	private String body;
	private String subject;
	private int priority;

	public Sms () { /* unused */ }

	public String getRecipient () { return recipient; }

	public void setRecipient ( String recipient ) { this.recipient = recipient; }

	public String getBody () { return body; }

	public void setBody ( String body ) { this.body = body; }

	public String getSubject () { return subject; }

	public void setSubject ( String subject ) { this.subject = subject; }

	public int getPriority () { return priority; }

	public void setPriority ( int priority ) { this.priority = priority; }
}
