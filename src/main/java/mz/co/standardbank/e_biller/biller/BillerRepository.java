package mz.co.standardbank.e_biller.biller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Repository
public interface BillerRepository extends JpaRepository<Biller, UUID> {
	Optional<Biller> findByName(String name);
}
