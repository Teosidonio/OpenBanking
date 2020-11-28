package mz.co.standardbank.e_biller.invoice;

import mz.co.standardbank.e_biller.biller.Biller;
import mz.co.standardbank.e_biller.client.Client;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Entity
public class Invoice {

	@Id
	@GeneratedValue
	private UUID id;
	@Column( nullable = false, updatable = false )
	private String invoiceNumber;
	@ManyToOne
	@JoinColumn( name = "contract", nullable = false, updatable = false )
	private Client client;
	@ManyToOne
	@JoinColumn( name = "biller", nullable = false, updatable = false )
	private Biller biller;
	@Column( nullable = false, updatable = false )
	private String description;
	@Column( nullable = false, updatable = false )
	private double amount;
	@Column( nullable = false, updatable = false )
	private LocalDateTime dateIssued;
	@Column( nullable = false, updatable = false )
	private LocalDateTime dateDue;
	@Column
	private LocalDateTime datePaid;
	@Column( nullable = false )
	private InvoiceStatus invoiceStatus;

	public UUID getId () { return id; }

	public void setId ( UUID id ) { this.id = id; }

	public String getInvoiceNumber () { return invoiceNumber; }

	public void setInvoiceNumber ( String invoiceNumber ) { this.invoiceNumber = invoiceNumber; }

	public Client getClient () { return client; }

	public void setClient ( Client client ) { this.client = client; }

	public Biller getBiller () { return biller; }

	public void setBiller ( Biller biller ) { this.biller = biller; }

	public String getDescription () { return description; }

	public void setDescription ( String description ) { this.description = description; }

	public double getAmount () { return amount; }

	public void setAmount ( double amount ) { this.amount = amount; }

	public LocalDateTime getDateIssued () { return dateIssued; }

	public void setDateIssued ( LocalDateTime issued ) { this.dateIssued = issued; }

	public LocalDateTime getDateDue () { return dateDue; }

	public void setDateDue ( LocalDateTime due ) { this.dateDue = due; }

	public LocalDateTime getDatePaid () { return datePaid; }

	public void setDatePaid ( LocalDateTime datePaid ) { this.datePaid = datePaid; }

	public InvoiceStatus getInvoiceStatus () { return invoiceStatus; }

	public void setInvoiceStatus ( InvoiceStatus invoiceStatus ) { this.invoiceStatus = invoiceStatus; }
}
