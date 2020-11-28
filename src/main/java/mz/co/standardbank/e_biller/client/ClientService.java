package mz.co.standardbank.e_biller.client;

import mz.co.standardbank.e_biller.biller.Biller;
import mz.co.standardbank.e_biller.biller.BillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * @author - C816346 on 2020/09/16
 */
@Service
public class ClientService {

	private ClientRepository clientRepository;
	private BillerService billerService;

	public Page<Client> findAll ( Pageable pageable ) {
		return clientRepository.findAll(pageable);
	}

	public Client find ( String contract ) {
		return clientRepository.findById(contract).orElseThrow(EntityNotFoundException::new);
	}

	public Client save ( String nib ) {
		if ( nib.isEmpty() ) throw new IllegalArgumentException();
		if ( clientRepository.existsById(nib) ) throw new EntityExistsException();
		Client client = new Client();
		client.setNib(nib);
		client.setActive(true);
		return clientRepository.save(client);
	}

	public Client setActive ( String contract , boolean isActive ) {
		Client client = clientRepository.findById(contract).orElseThrow(EntityNotFoundException::new);
		client.setActive(isActive);
		return clientRepository.save(client);
	}

	public Client registerBiller ( String contract , String name ) {
		Client client = clientRepository.findById(contract).orElseThrow(EntityNotFoundException::new);
		Biller biller = billerService.findByName(name);
		client.addBiller(biller);
		return clientRepository.save(client);
	}

	public Client unregisterBiller(String contract, String name){
		Client client = clientRepository.findById(contract).orElseThrow(EntityNotFoundException::new);
		Biller biller = billerService.findByName(name);
		client.removeBiller(biller);
		return clientRepository.save(client);
	}

	public void delete ( String contract ) {
		if ( contract.isEmpty() ) throw new IllegalArgumentException();
		if ( !clientRepository.existsById(contract) ) throw new EntityNotFoundException();
		clientRepository.deleteById(contract);
	}


	@Autowired
	public void setClientRepository ( ClientRepository clientRepository ) { this.clientRepository = clientRepository; }

	@Autowired
	public void setBillerService ( BillerService billerService ) { this.billerService = billerService; }
}
