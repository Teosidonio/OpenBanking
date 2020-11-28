package mz.co.standardbank.e_biller.invoice;

/**
 * @author - C816346 on 2020/09/17
 */
public class FailedInvoiceDao {
	private InvoiceRequest invoiceDao;
	private String message;

	public InvoiceRequest getInvoiceDao () { return invoiceDao; }

	public void setInvoiceDao ( InvoiceRequest invoiceDao ) { this.invoiceDao = invoiceDao; }

	public String getMessage () { return message; }

	public void setMessage ( String message ) { this.message = message; }
}
