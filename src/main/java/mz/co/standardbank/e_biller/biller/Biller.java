package mz.co.standardbank.e_biller.biller;

import mz.co.standardbank.e_biller.client.Client;
import mz.co.standardbank.e_biller.invoice.Invoice;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Entity
public class Biller {

	@Id
	@GeneratedValue
	private UUID apiKey;
	@Column( unique = true, nullable = false )
	private String name;
	@Column( unique = true, nullable = false )
	private String accountNumber;
	@Column( unique = true, nullable = false )
	private String url;
	@ManyToMany( mappedBy = "billers", fetch = FetchType.LAZY )
	private Set<Client> clients;
	@OneToMany( mappedBy = "biller", fetch = FetchType.LAZY )
	private Set<Invoice> invoices;

	public UUID getApiKey () { return apiKey; }

	public void setApiKey ( UUID apiKey ) { this.apiKey = apiKey; }

	public String getName () { return name; }

	public void setName ( String name ) { this.name = name; }

	public String getAccountNumber () { return accountNumber; }

	public void setAccountNumber ( String contract ) { this.accountNumber = contract; }

	public String getUrl () { return url; }

	public void setUrl ( String url ) { this.url = url; }

	public Set<Client> getClients () { return clients; }

	public void setClients ( Set<Client> clients ) { this.clients = clients; }

	public Set<Invoice> getInvoices () { return invoices; }

	public void setInvoices ( Set<Invoice> invoices ) { this.invoices = invoices; }
}
