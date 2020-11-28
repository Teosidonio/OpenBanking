package mz.co.standardbank.e_biller.invoice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author - C816346 on 2020/09/17
 */
public class InvoiceIssueDao {
	private List<InvoiceDto> successful;
	private List<FailedInvoiceDao> failed;

	public InvoiceIssueDao () {
		successful = new ArrayList<>();
		failed = new ArrayList<>();
	}

	public void addToSuccessful ( InvoiceDto invoiceDto ) { successful.add(invoiceDto); }

	public void addToFailed ( FailedInvoiceDao failedInvoiceDao ) { failed.add(failedInvoiceDao); }

	public int getSuccessfulSize () { return successful.size(); }
}
