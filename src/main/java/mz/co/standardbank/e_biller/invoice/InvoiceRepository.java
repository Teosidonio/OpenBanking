package mz.co.standardbank.e_biller.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
	boolean existsByBillerApiKeyAndInvoiceNumber ( UUID apiKey , String invoiceNumber );

	Invoice findByInvoiceNumber ( String invoiceNumber );

	Page<Invoice> findAllByBillerApiKey ( UUID apiKey , Pageable pageable );

	Page<Invoice> findAllByBillerApiKeyAndInvoiceStatus (
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
														);

	Page<Invoice> findAllByClientNib ( String nib , Pageable pageable );

	Page<Invoice> findAllByClientNibAndInvoiceStatus (
			String nib ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
														  );

	Page<Invoice> findAllByClientNibAndDateIssued (
			String nib ,
			LocalDateTime dateIssued ,
			Pageable pageable
													   );

	Page<Invoice> findAllByClientNibAndDateDue ( String nib , LocalDateTime dateDue , Pageable pageable );

	Page<Invoice> findAllByClientNibAndBillerApiKey ( String nib , UUID apiKey , Pageable pageable );

	Page<Invoice> findAllByClientNibAndBillerApiKeyAndInvoiceStatus (
			String nib ,
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
																		 );

	Page<Invoice> findAllByClientNibAndBillerApiKeyAndDateIssued (
			String nib ,
			UUID apiKey ,
			LocalDateTime dateIssued ,
			Pageable pageable
																	  );

	Page<Invoice> findAllByClientNibAndBillerApiKeyAndDateDue (
			String nib ,
			UUID apiKey ,
			LocalDateTime dateDue ,
			Pageable pageable
																   );

	Page<Invoice> findAllByClientNibAndBillerApiKeyAndDatePaid (
			String nib ,
			UUID apiKey ,
			LocalDateTime datePaid ,
			Pageable pageable
																	);

}
