package mz.co.standardbank.e_biller.biller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Service
public class BillerService {
	private BillerRepository repository;

	public Page<Biller> findAll ( Pageable pageable ) { return repository.findAll(pageable); }

	public Biller findByName ( String name ) {
		return repository.findByName(name).orElseThrow(EntityNotFoundException::new);
	}

	public Biller findByApiKey ( UUID apiKey ) {
		return repository.findById(apiKey).orElseThrow(EntityNotFoundException::new);
	}

	public Biller save ( BillerDto billerDto ) { return repository.save(mapToBiller(billerDto)); }

	public void delete ( UUID apiKey ) { repository.deleteById(apiKey); }

	private Biller mapToBiller ( BillerDto billerDto ) {
		Biller biller = new Biller();
		biller.setAccountNumber(billerDto.getAccountNumber());
		biller.setName(billerDto.getName());
		biller.setUrl(billerDto.getUrl());
		return biller;
	}

	@Autowired
	public void setBillerRepository ( BillerRepository repository ) {
		this.repository = repository;
	}
}
