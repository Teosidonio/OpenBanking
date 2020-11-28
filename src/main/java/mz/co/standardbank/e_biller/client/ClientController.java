package mz.co.standardbank.e_biller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * @author - C816346 on 2020/09/16
 */
@RestController
@RequestMapping( "client" )
public class ClientController {
	private ClientService service;

	/**
	 * Subscribes a client to the service based on the {@param contract} by registering them to the database
	 * and setting their status to active
	 *
	 * @param contract the client contract used to look up information on the T24 end
	 * @return {@code ResponseEntity} containing the status and client
	 */
	@PostMapping( "/subscribe/{contract}" )
	public ResponseEntity<ClientDto> subscribe ( @PathVariable String contract ) {
		try {
			return new ResponseEntity<>(mapToClientDto(service.save(contract)) , HttpStatus.OK);
		} catch ( EntityExistsException | IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Unsubscribe a client from the service based on {@param contract} by setting active
	 * status to false
	 *
	 * @param contract the client contract
	 * @return {@code ResponseEntity} describing the success
	 */
	@PostMapping( "/unsubscribe/{contract}" )
	public ResponseEntity<ClientDto> unsubscribe ( @PathVariable String contract ) {
		try {
			return new ResponseEntity<>(mapToClientDto(service.setActive(contract , false)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Adds a {@code Biller} to the list of acceptable issuers of invoices for the {@code Client}
	 *
	 * @param contract the {@code Client} contract number
	 * @param biller   the {@code Biller} name
	 * @return {@code ResponseEntity} describing the success and the {@code Client}
	 */
	@PostMapping( "/{contract}/register/{biller}" )
	public ResponseEntity<ClientDto> registerBiller ( @PathVariable String contract , @PathVariable String biller ) {
		try {
			return new ResponseEntity<>(mapToClientDto(service.registerBiller(contract , biller)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Removes a {@code Biller} from the list of acceptable issuers of invoices for the {@code Client}
	 *
	 * @param contract the {@code Client} contract number
	 * @param biller   the {@code Biller} name
	 * @return {@code ResponseEntity} describing the success and the {@code Client}
	 */
	@PostMapping( "/{contract}/unregister/{biller}" )
	public ResponseEntity<ClientDto> unregisterBiller ( @PathVariable String contract , @PathVariable String biller ) {
		try {
			return new ResponseEntity<>(mapToClientDto(service.unregisterBiller(contract , biller)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}


	/**
	 * Looks up a {@code Page} of clients from the database
	 *
	 * @param page the page number
	 * @param size the number of entities per page
	 * @return {@code ResponseEntity} containing the {@code Page} and {@code HttpStatus}
	 */
	@GetMapping( "/find/all" )
	public ResponseEntity<Page<ClientDto>> findAll ( @RequestParam int page , @RequestParam int size ) {
		try {
			return new ResponseEntity<>(service.findAll(PageRequest.of(page , size)).map(this::mapToClientDto) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	/**
	 * Looks up a single {@code Client} by {@param contract}
	 *
	 * @param contract the {@code Client} identifier
	 * @return {@code ResponseEntity} containing the {@code Client} and {@code HttpStatus}
	 */
	@GetMapping( "/find/{contract}" )
	public ResponseEntity<ClientDto> findByContract ( @PathVariable String contract ) {
		try {
			return new ResponseEntity<>(mapToClientDto(service.find(contract)) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	private ClientDto mapToClientDto ( Client client ) {
		ClientDto clientDto = new ClientDto();
		clientDto.setNib(client.getNib());
		clientDto.setActive(client.isActive());
		return clientDto;
	}

	@Autowired
	public void setService ( ClientService service ) { this.service = service; }
}
