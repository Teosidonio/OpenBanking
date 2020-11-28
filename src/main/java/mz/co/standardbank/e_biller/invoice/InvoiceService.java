package mz.co.standardbank.e_biller.invoice;

import mz.co.standardbank.e_biller.biller.Biller;
import mz.co.standardbank.e_biller.biller.BillerService;
import mz.co.standardbank.e_biller.client.Client;
import mz.co.standardbank.e_biller.client.ClientService;
import mz.co.standardbank.e_biller.customer_details.CustomerDetailApi;
import mz.co.standardbank.e_biller.customer_details.CustomerDetails;
import mz.co.standardbank.e_biller.sms.Sms;
import mz.co.standardbank.e_biller.sms.SmsGateway;
import mz.co.standardbank.e_biller.transfer.IntrabankTransferApi;
import mz.co.standardbank.e_biller.transfer.TransferRequest;
import org.apache.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Service
public class InvoiceService {
	private Logger logger = LogManager.getLogger(this);

	private InvoiceRepository invoiceRepository;
	private BillerService billerService;
	private ClientService clientService;
	private SmsGateway smsGateway;
	private CustomerDetailApi customerDetailApi;
	private IntrabankTransferApi intrabankTransferApi;

	@Value( "${sms.message.template}" )
	private String TEMPLATE;
	@Value( "${sms.queue.priority}" )
	private int PRIORITY;
	@Value( "$sms.message.subject" )
	private String SUBJECT;

	public InvoiceIssueDao issueInvoices ( List<InvoiceRequest> invoiceDaoList ) {
		logger.info("Validating {} invoices..." , invoiceDaoList.size());
		InvoiceIssueDao invoiceIssueDao = new InvoiceIssueDao();
		for ( InvoiceRequest invoiceDao : invoiceDaoList ) {
			logger.info("Validating invoice {}..." , invoiceDao.getInvoiceNumber());
			try {
				invoiceIssueDao.addToSuccessful(mapToInvoiceDto(save(invoiceDao)));
				Client client = clientService.find(invoiceDao.getClient());
				//TODO create custom exception
				if ( !client.isActive() ) throw new Exception("Client " + client + " inactive.");
				CustomerDetails customerDetails = customerDetailApi.getCustomerDetails(invoiceDao.getClient());
				String number = customerDetails.getPhone();
				logger.info("Attempting to forward message for invoice {}..." , invoiceDao.getInvoiceNumber());
				smsGateway.forwardPayload(mapToSms(number , mapToMessage(invoiceDao) , mapToSubject(invoiceDao)));
				logger.info("Invoice {} validated successfully" , invoiceDao.getInvoiceNumber());
			} catch ( Exception e ) {
				logger.info("Invoice {} invalid" , invoiceDao.getInvoiceNumber());
				FailedInvoiceDao failedInvoiceDao = new FailedInvoiceDao();
				failedInvoiceDao.setInvoiceDao(invoiceDao);
				failedInvoiceDao.setMessage(e.getMessage());
				invoiceIssueDao.addToFailed(failedInvoiceDao);
			}
		}
		logger.info("Invoices validated, {}/{} passed." , invoiceIssueDao.getSuccessfulSize() , invoiceDaoList.size());
		return invoiceIssueDao;
	}

	public Invoice acceptInvoice ( UUID id , String netTxnId , String netUsername ) throws HttpException {
		logger.info("Accepting invoice {}..." , id);
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		if ( invoice.getInvoiceStatus() != InvoiceStatus.PENDING ) throw new IllegalArgumentException();
		CustomerDetails customerDetails = customerDetailApi.getCustomerDetails(invoice.getClient().getNib());
		logger.info("Mapping information for transfer...");
		intrabankTransferApi.intrabankTransfer(
				mapToTransferRequest(customerDetails , invoice , netTxnId , netUsername));
		invoice.setDatePaid(LocalDateTime.now());
		invoice.setInvoiceStatus(InvoiceStatus.ACCEPTED);
		logger.info("Invoice {} accepted successfully." , id);
		return invoiceRepository.save(invoice);
	}

	public Invoice denyInvoice ( UUID id ) {
		logger.info("Rejecting invoice {}..." , id);
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		if ( invoice.getInvoiceStatus() != InvoiceStatus.PENDING ) throw new IllegalArgumentException();
		invoice.setInvoiceStatus(InvoiceStatus.DENIED);
		//TODO notify biller
		logger.info("Invoice {} rejected successfully." , id);
		return invoiceRepository.save(invoice);
	}

	public Invoice cancelInvoice ( UUID id ) {
		logger.info("Cancelling invoice {}..." , id);
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		if ( invoice.getInvoiceStatus() != InvoiceStatus.PENDING ) throw new IllegalArgumentException();
		invoice.setInvoiceStatus(InvoiceStatus.CANCELLED);
		logger.info("Invoice {} cancelled successfully." , id);
		return invoiceRepository.save(invoice);
	}

	private Invoice save ( InvoiceRequest invoiceDao ) throws EntityNotFoundException, IllegalArgumentException {
		if ( invoiceRepository.existsByBillerApiKeyAndInvoiceNumber(
				invoiceDao.getBiller() , invoiceDao.getInvoiceNumber()) )
			throw new EntityExistsException();
		Client client = clientService.find(invoiceDao.getClient());
		Biller biller = billerService.findByApiKey(invoiceDao.getBiller());
		if ( !client.hasBiller(biller) ) throw new IllegalArgumentException();
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber(invoiceDao.getInvoiceNumber());
		invoice.setClient(client);
		invoice.setBiller(biller);
		invoice.setDescription(invoiceDao.getDescription());
		invoice.setAmount(invoiceDao.getAmount());
		invoice.setDateDue(invoiceDao.getDateDue());
		invoice.setDateIssued(LocalDateTime.now());
		invoice.setInvoiceStatus(InvoiceStatus.PENDING);
		return invoice;
	}

	public Page<Invoice> findAll ( Pageable pageable ) {
		return invoiceRepository.findAll(pageable);
	}

	public Invoice findById ( UUID id ) {
		return invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
	}

	public Page<Invoice> findAllByBiller ( UUID apiKey , Pageable pageable ) {
		return invoiceRepository.findAllByBillerApiKey(apiKey , pageable);
	}

	public Page<Invoice> findAllByBillerAndStatus (
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
												  ) {
		return invoiceRepository.findAllByBillerApiKeyAndInvoiceStatus(apiKey , invoiceStatus , pageable);

	}

	public Page<Invoice> findAllByClient ( String contract , Pageable pageable ) {
		return invoiceRepository.findAllByClientNib(contract , pageable);
	}

	public Page<Invoice> findAllByClientAndStatus (
			String contract ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
												  ) {
		return invoiceRepository.findAllByClientNibAndInvoiceStatus(contract , invoiceStatus , pageable);
	}

	public Page<Invoice> findAllByClientAndIssued (
			String contract ,
			LocalDateTime dateIssued ,
			Pageable pageable
												  ) {
		return invoiceRepository.findAllByClientNibAndDateDue(contract , dateIssued , pageable);
	}

	public Page<Invoice> findAllByClientAndDue ( String contract , LocalDateTime dateDue , Pageable pageable ) {
		return invoiceRepository.findAllByClientNibAndDateDue(contract , dateDue , pageable);
	}

	public Page<Invoice> findAllByClientAndBiller ( String contract , UUID apiKey , Pageable pageable ) {
		return invoiceRepository.findAllByClientNibAndBillerApiKey(contract , apiKey , pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndStatus (
			String contract ,
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
														   ) {
		return invoiceRepository.findAllByClientNibAndBillerApiKeyAndInvoiceStatus(
				contract , apiKey , invoiceStatus , pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndIssued (
			String contract ,
			UUID apiKey ,
			LocalDateTime dateIssued ,
			Pageable pageable
														   ) {
		return invoiceRepository.findAllByClientNibAndBillerApiKeyAndDateIssued(
				contract , apiKey , dateIssued , pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndDue (
			String contract ,
			UUID apiKey ,
			LocalDateTime dateDue ,
			Pageable pageable
														) {
		return invoiceRepository.findAllByClientNibAndBillerApiKeyAndDateDue(contract , apiKey , dateDue ,
																			 pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndPaid (
			String contract ,
			UUID apiKey ,
			LocalDateTime datePaid ,
			Pageable pageable
														 ) {
		return invoiceRepository.findAllByClientNibAndBillerApiKeyAndDatePaid(
				contract , apiKey , datePaid , pageable);
	}

	//TODO maybe change this to a proper templating package for more intricate message creation
	private String mapToMessage ( InvoiceRequest invoiceRequest ) {
		String billerName = billerService.findByApiKey(invoiceRequest.getBiller()).getName();
		return String.format(TEMPLATE , invoiceRequest.getAmount() , billerName , invoiceRequest.getDescription() ,
							 invoiceRequest.getDateDue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
	}

	private String mapToSubject ( InvoiceRequest invoiceDao ) {
		return String.format(SUBJECT , invoiceDao.getInvoiceNumber());
	}

	private Sms mapToSms ( String number , String message , String subject ) {
		Sms sms = new Sms();
		if ( !number.startsWith("+") ) number = "+".concat(number);
		sms.setRecipient(number);
		sms.setBody(message);
		sms.setSubject(subject);
		sms.setPriority(PRIORITY);
		return sms;
	}

	public static InvoiceDto mapToInvoiceDto ( Invoice invoice ) {
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setId(invoice.getId());
		invoiceDto.setInvoiceNumber(invoice.getInvoiceNumber());
		invoiceDto.setClient(invoice.getClient().getNib());
		invoiceDto.setBiller(invoice.getBiller().getName());
		invoiceDto.setDescription(invoice.getDescription());
		invoiceDto.setAmount(invoice.getAmount());
		invoiceDto.setDateIssued(invoice.getDateIssued());
		invoiceDto.setDateDue(invoice.getDateDue());
		invoiceDto.setDatePaid(invoice.getDatePaid());
		invoiceDto.setInvoiceStatus(invoice.getInvoiceStatus());
		return invoiceDto;
	}

	private TransferRequest mapToTransferRequest (
			CustomerDetails customerDetails ,
			Invoice invoice ,
			String netTxnId ,
			String netUsername
												 ) {
		TransferRequest transferRequest = new TransferRequest();
		transferRequest.setDebitAcctNo(customerDetails.getAccountNo());
		transferRequest.setDebitCurrency("MZN");
		transferRequest.setDebitAmount(invoice.getAmount() + "");
		transferRequest.setDebitTheirRef(invoice.getInvoiceNumber());
		transferRequest.setCreditAcctNo(invoice.getBiller().getAccountNumber());
		transferRequest.setCreditCurrency("MZN");
		transferRequest.setPaymentDetails(invoice.getDescription());
		transferRequest.setCreditTheirRef(invoice.getInvoiceNumber());
		transferRequest.setNetTxnId(netTxnId);
		transferRequest.setNetUsername(netUsername);
		transferRequest.setCustomerId(customerDetails.getLegalID());
		transferRequest.setRequestDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		transferRequest.setTelNo(customerDetails.getPhone());
		transferRequest.setEmailAddress(customerDetails.getEmail());
		transferRequest.setChannel("3"); // TODO fill this in
		return transferRequest;
	}

	@Autowired
	public void setInvoiceRepository ( InvoiceRepository invoiceRepository ) { this.invoiceRepository = invoiceRepository; }

	@Autowired
	public void setBillerService ( BillerService billerService ) { this.billerService = billerService; }

	@Autowired
	public void setClientService ( ClientService clientService ) { this.clientService = clientService; }

	@Autowired
	public void setSmsGateway ( SmsGateway smsGateway ) { this.smsGateway = smsGateway; }

	@Autowired
	public void setCustomerDetailApi ( CustomerDetailApi customerDetailApi ) { this.customerDetailApi = customerDetailApi; }

	@Autowired
	public void setIntrabankTransferApi ( IntrabankTransferApi intrabankTransferApi ) {this.intrabankTransferApi = intrabankTransferApi; }
}
