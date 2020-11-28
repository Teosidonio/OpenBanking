package mz.co.standardbank.e_biller.biller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@RestController
@RequestMapping( "biller" )
public class BillerController {

	private BillerService service;

	/**
	 * Looks up a {@code Page} of {@code Biller} from the database
	 *
	 * @param page page number
	 * @param size page size
	 * @return {@code ResponseEntity} containing the status and {@code Biller} list
	 */
	@GetMapping( "/find/all" )
	public ResponseEntity<Page<BillerDto>> findAll ( @RequestParam int page , @RequestParam int size ) {
		try {
			return new ResponseEntity<>(
					service.findAll(PageRequest.of(page , size)).map(this::mapToBillerDto) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Looks up a {@code Biller} by name from the database
	 *
	 * @param name {@code Biller} name for lookup
	 * @return @return {@code ResponseEntity} containing the status and {@code Biller}
	 */
	@GetMapping( "/find/{name}" )
	public ResponseEntity<BillerDto> findByName ( @PathVariable String name ) {
		try {
			return new ResponseEntity<>(mapToBillerDto(service.findByName(name)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Looks up a {@code Biller} by their API key from the database
	 *
	 * @param key {@code Biller} name for lookup
	 * @return @return {@code ResponseEntity} containing the status and {@code Biller}
	 */
	@GetMapping( "/find/{key}" )
	public ResponseEntity<BillerDto> findByApiKey ( @PathVariable UUID key ) {
		try {
			return new ResponseEntity<>(mapToBillerDto(service.findByApiKey(key)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Subscribes a {@code Biller} to the system
	 *
	 * @param billerDto object containing pertinent information for registration
	 * @return {@code ResponseEntity} containing the status and {@code Biller}
	 */
	@PostMapping( "/subscribe" )
	public ResponseEntity<Biller> subscribe ( @RequestBody BillerDto billerDto ) {
		try {
			return new ResponseEntity<>(service.save(billerDto) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Unsubscribe a {@code Biller} from the system
	 *
	 * @param key to identify the biller
	 * @return {@code ResponseEntity} containing the status
	 */
	@DeleteMapping( "/unsubscribe/{key}" )
	public ResponseEntity<HttpStatus> unsubscribe ( @PathVariable UUID key ) {
		try {
			service.delete(key);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	private BillerDto mapToBillerDto ( Biller biller ) {
		BillerDto billerDto = new BillerDto();
		billerDto.setName(biller.getName());
		billerDto.setAccountNumber(biller.getAccountNumber());
		billerDto.setUrl(biller.getUrl());
		return billerDto;
	}

	@Autowired
	public void setService ( BillerService service ) { this.service = service; }
}
