package mz.co.standardbank.e_biller.configuration;

import com.sun.istack.NotNull;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author - C816346 on 2020/05/06
 */
@CrossOrigin( methods = { RequestMethod.POST } )
@RestController
public class ConfigurationController {

	//TODO restrict access to these endpoints

	/**
	 * Sets the log {@param level}
	 *
	 * @param level of the logging
	 * @return {@code HttpStatus}
	 */
	@PostMapping( name = "Set Log Level", value = "/logging/{level}" )
	@NotNull
	public ResponseEntity<HttpStatus> setLogLevel ( @PathVariable Level level ) {
		try {
			Configurator.setAllLevels(LogManager.getRootLogger().getName() , level);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

}
