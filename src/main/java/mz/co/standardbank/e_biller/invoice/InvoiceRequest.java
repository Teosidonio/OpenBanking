package mz.co.standardbank.e_biller.invoice;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
public class InvoiceRequest {
	private String invoiceNumber;
	private String client;
	private UUID biller;
	private String description;
	private double amount;
	private LocalDateTime dateDue;

	public String getInvoiceNumber () { return invoiceNumber; }

	public void setInvoiceNumber ( String invoiceNumber ) { this.invoiceNumber = invoiceNumber; }

	public String getClient () { return client; }

	public void setClient ( String client ) { this.client = client; }

	public UUID getBiller () { return biller; }

	public void setBiller ( UUID biller ) { this.biller = biller; }

	public String getDescription () { return description; }

	public void setDescription ( String description ) { this.description = description; }

	public double getAmount () { return amount; }

	public void setAmount ( double amount ) { this.amount = amount; }

	public LocalDateTime getDateDue () { return dateDue; }

	public void setDateDue ( LocalDateTime dateDue ) { this.dateDue = dateDue; }
}
