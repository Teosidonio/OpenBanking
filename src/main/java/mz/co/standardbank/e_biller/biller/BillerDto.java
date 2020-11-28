package mz.co.standardbank.e_biller.biller;

/**
 * @author - C816346 on 2020/09/16
 */
public class BillerDto {
	private String name;
	private String accountNumber;
	private String url;

	public String getName () { return name; }

	public void setName ( String name ) { this.name = name; }

	public String getAccountNumber () { return accountNumber; }

	public void setAccountNumber ( String accountNumber ) { this.accountNumber = accountNumber; }

	public String getUrl () { return url; }

	public void setUrl ( String url ) { this.url = url; }
}
