package mz.co.standardbank.e_biller.transfer;

/**
 * @author - C816346 on 2020/11/08
 */
public class TransferRequest {

	private String debitAcctNo;
	private String debitCurrency;
	private String debitAmount;
	private String debitTheirRef;
	private String creditAcctNo;
	private String creditCurrency;
	private String paymentDetails;
	private String creditTheirRef;
	private String netTxnId;
	private String netUsername;
	private String customerId;
	private String requestDate;
	private String telNo;
	private String emailAddress;
	private String channel;

	public String getDebitAcctNo () {
		return debitAcctNo;
	}

	public void setDebitAcctNo ( String debitAcctNo ) {
		this.debitAcctNo = debitAcctNo;
	}

	public String getDebitCurrency () {
		return debitCurrency;
	}

	public void setDebitCurrency ( String debitCurrency ) {
		this.debitCurrency = debitCurrency;
	}

	public String getDebitAmount () {
		return debitAmount;
	}

	public void setDebitAmount ( String debitAmount ) {
		this.debitAmount = debitAmount;
	}

	public String getDebitTheirRef () {
		return debitTheirRef;
	}

	public void setDebitTheirRef ( String debitTheirRef ) {
		this.debitTheirRef = debitTheirRef;
	}

	public String getCreditAcctNo () {
		return creditAcctNo;
	}

	public void setCreditAcctNo ( String creditAcctNo ) {
		this.creditAcctNo = creditAcctNo;
	}

	public String getCreditCurrency () {
		return creditCurrency;
	}

	public void setCreditCurrency ( String creditCurrency ) {
		this.creditCurrency = creditCurrency;
	}

	public String getPaymentDetails () {
		return paymentDetails;
	}

	public void setPaymentDetails ( String paymentDetails ) {
		this.paymentDetails = paymentDetails;
	}

	public String getCreditTheirRef () {
		return creditTheirRef;
	}

	public void setCreditTheirRef ( String creditTheirRef ) {
		this.creditTheirRef = creditTheirRef;
	}

	public String getNetTxnId () {
		return netTxnId;
	}

	public void setNetTxnId ( String netTxnId ) {
		this.netTxnId = netTxnId;
	}

	public String getNetUsername () {
		return netUsername;
	}

	public void setNetUsername ( String netUsername ) {
		this.netUsername = netUsername;
	}

	public String getCustomerId () {
		return customerId;
	}

	public void setCustomerId ( String customerId ) {
		this.customerId = customerId;
	}

	public String getRequestDate () {
		return requestDate;
	}

	public void setRequestDate ( String requestDate ) {
		this.requestDate = requestDate;
	}

	public String getTelNo () {
		return telNo;
	}

	public void setTelNo ( String telNo ) {
		this.telNo = telNo;
	}

	public String getEmailAddress () {
		return emailAddress;
	}

	public void setEmailAddress ( String emailAddress ) {
		this.emailAddress = emailAddress;
	}

	public String getChannel () {
		return channel;
	}

	public void setChannel ( String channel ) {
		this.channel = channel;
	}
}
