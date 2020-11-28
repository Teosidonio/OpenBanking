package mz.co.standardbank.e_biller.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@RestController
@RequestMapping( "invoice" )
public class InvoiceController {
	private InvoiceService service;

	@PostMapping( "/issue" )
	public ResponseEntity<InvoiceIssueDao> issueInvoices ( @RequestBody List<InvoiceRequest> invoiceDaoList ) {
		try {
			return new ResponseEntity<>(service.issueInvoices(invoiceDaoList) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@PostMapping( "/{id}/accept" )
	public ResponseEntity<Invoice> acceptInvoice (
			@PathVariable UUID id ,
			@RequestParam( defaultValue = "" ) String netTxnId ,
			@RequestParam( defaultValue = "" ) String netUsername
												 ) {
		try {
			return new ResponseEntity<>(service.acceptInvoice(id , netTxnId , netUsername) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@PostMapping( "/{id}/deny" )
	public ResponseEntity<Invoice> denyInvoice ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(service.denyInvoice(id) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@PostMapping( "/{id}/cancel" )
	public ResponseEntity<Invoice> cancelInvoice ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(service.cancelInvoice(id) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/all" )
	public ResponseEntity<Page<InvoiceDto>> findAll (
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
													) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAll(pageable).map(InvoiceService::mapToInvoiceDto) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/{id}" )
	public ResponseEntity<InvoiceDto> findById ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(InvoiceService.mapToInvoiceDto(service.findById(id)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/key/{key}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByBiller (
			@PathVariable UUID key ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
															) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByBiller(key , pageable).map(InvoiceService::mapToInvoiceDto) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/{key}/status/{status}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByBillerAndStatus (
			@PathVariable UUID key ,
			@PathVariable InvoiceStatus status ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																	 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByBillerAndStatus(key , status , pageable).map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/client/{contract}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClient (
			@PathVariable String contract ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
															) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByClient(contract , pageable).map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/client/{contract}/status/{status}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndStatus (
			@PathVariable String contract ,
			@PathVariable InvoiceStatus status ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																	 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByClientAndStatus(contract , status , pageable)
						   .map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/client/{contract}/issues/{issued}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndIssued (
			@PathVariable String contract ,
			@PathVariable LocalDateTime issued ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																	 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByClientAndIssued(contract , issued , pageable)
						   .map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/client/{contract}/dues/{due}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndDue (
			@PathVariable String contract ,
			@PathVariable LocalDateTime due ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByClientAndDue(contract , due , pageable).map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/{key}/client/{contract}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndBiller (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																	 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByClientAndBiller(contract , key , pageable).map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/{key}/client/{contract}/status/{status}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndBillerAndStatus (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable InvoiceStatus status ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																			  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndStatus(contract , key , status , pageable)
											   .map(InvoiceService::mapToInvoiceDto) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/{key}/client/{contract}/issued/{issued}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndBillerAndIssued (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable LocalDateTime issued ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																			  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndIssued(contract , key , issued , pageable)
											   .map(InvoiceService::mapToInvoiceDto) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/{key}/client/{contract}/due/{due}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndBillerAndDue (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable LocalDateTime due ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																		   ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(
					service.findAllByClientAndBillerAndDue(contract , key , due , pageable)
						   .map(InvoiceService::mapToInvoiceDto) ,
					HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "find/biller/{key}/client/{contract}/paid/{paid}" )
	public ResponseEntity<Page<InvoiceDto>> findAllByClientAndBillerAndPaid (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable LocalDateTime paid ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																			) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndPaid(contract , key , paid , pageable)
											   .map(InvoiceService::mapToInvoiceDto) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}


	@Autowired
	public void setService ( InvoiceService service ) { this.service = service; }
}
