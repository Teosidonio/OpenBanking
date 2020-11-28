package mz.co.standardbank.e_biller.client;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author - C816346 on 2020/09/16
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
}
