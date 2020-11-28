package mz.co.standardbank.e_biller.client;

import mz.co.standardbank.e_biller.biller.Biller;
import mz.co.standardbank.e_biller.invoice.Invoice;

import javax.persistence.*;
import java.util.Set;

/**
 * @author - C816346 on 2020/09/16
 */
@Entity
public class Client {

	@Id
	private String nib;
	@Column
	private boolean active;
	@ManyToMany( fetch = FetchType.LAZY )
	private Set<Biller> billers;
	@OneToMany( mappedBy = "client", fetch = FetchType.LAZY )
	private Set<Invoice> invoices;

	public String getNib () { return nib; }

	public void setNib ( String contract ) { this.nib = contract; }

	public boolean isActive () { return active; }

	public void setActive ( boolean active ) { this.active = active; }

	public Set<Biller> getBillers () { return billers; }

	public void setBillers ( Set<Biller> billers ) { this.billers = billers; }

	public Set<Invoice> getInvoices () { return invoices; }

	public void setInvoices ( Set<Invoice> invoices ) { this.invoices = invoices; }

	public void addBiller ( Biller biller ) { billers.add(biller); }

	public void removeBiller ( Biller biller ) { billers.remove(biller); }

	public boolean hasBiller ( Biller biller ) { return billers.contains(biller); }
}
