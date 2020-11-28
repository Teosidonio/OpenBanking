package mz.co.standardbank.e_biller.customer_details;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;

/**
 * @author - C816346 on 2020/11/08
 */
@Component
public class CustomerDetailApi {

	private Logger logger = LogManager.getLogger(this);

	@Value( "${t24.base.url}" )
	private String baseUrl;
	@Value( "${t24.customer.query}" )
	private String action;
	private ObjectMapper objectMapper;

	public CustomerDetails getCustomerDetails ( String nib ) throws HttpException {
		try {
			logger.info("Querying customer details for {}..." , nib);
			//builds the post string
			String url = baseUrl.concat(action).concat(nib);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet httpGet = new HttpGet(url);
			//executes the request
			HttpResponse httpResponse = httpClient.execute(httpGet);
			//handle response
			String entityString = EntityUtils.toString(httpResponse.getEntity());
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			//if the response code is anything but OK (200), an exception is thrown
			if ( statusCode != 200 ) throw new HttpException(statusCode + " " + entityString);
			logger.info("Query successful");
			return objectMapper.readValue(entityString , CustomerDetails.class);
		}  catch ( IOException e ) {
			throw new HttpException("Failed to map customer details response." , e);
		}
	}

	@Autowired
	public void setObjectMapper ( ObjectMapper objectMapper ) { this.objectMapper = objectMapper; }
}
