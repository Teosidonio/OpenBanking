package mz.co.standardbank.e_biller.invoice;

import mz.co.standardbank.e_biller.biller.Biller;
import mz.co.standardbank.e_biller.client.Client;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author - C816346 on 2020/11/09
 */
public class InvoiceDto {
	private UUID id;
	private String invoiceNumber;
	private String client;
	private String biller;
	private String description;
	private double amount;
	private LocalDateTime dateIssued;
	private LocalDateTime dateDue;
	private LocalDateTime datePaid;
	private InvoiceStatus invoiceStatus;

	public UUID getId () {
		return id;
	}

	public void setId ( UUID id ) {
		this.id = id;
	}

	public String getInvoiceNumber () {
		return invoiceNumber;
	}

	public void setInvoiceNumber ( String invoiceNumber ) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getClient () {
		return client;
	}

	public void setClient ( String client ) {
		this.client = client;
	}

	public String getBiller () {
		return biller;
	}

	public void setBiller ( String biller ) {
		this.biller = biller;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription ( String description ) {
		this.description = description;
	}

	public double getAmount () {
		return amount;
	}

	public void setAmount ( double amount ) {
		this.amount = amount;
	}

	public LocalDateTime getDateIssued () {
		return dateIssued;
	}

	public void setDateIssued ( LocalDateTime dateIssued ) {
		this.dateIssued = dateIssued;
	}

	public LocalDateTime getDateDue () {
		return dateDue;
	}

	public void setDateDue ( LocalDateTime dateDue ) {
		this.dateDue = dateDue;
	}

	public LocalDateTime getDatePaid () {
		return datePaid;
	}

	public void setDatePaid ( LocalDateTime datePaid ) {
		this.datePaid = datePaid;
	}

	public InvoiceStatus getInvoiceStatus () {
		return invoiceStatus;
	}

	public void setInvoiceStatus ( InvoiceStatus invoiceStatus ) {
		this.invoiceStatus = invoiceStatus;
	}
}
