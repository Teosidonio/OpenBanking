package mz.co.standardbank.e_biller.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author - C816346 on 2020/11/08
 */
@Component
public class IntrabankTransferApi {
	private Logger logger = LogManager.getLogger(this);

	@Value( "${t24.base.url}" )
	private String baseUrl;
	@Value( "${t24.intrabank.transfer}" )
	private String action;
	private ObjectMapper objectMapper;

	public TransferResponse intrabankTransfer ( TransferRequest request ) throws HttpException {
		try {
			logger.info(
					"Preparing for intrabank transfer to {} from {}..." , request.getCreditAcctNo() ,
					request.getDebitAcctNo()
					   );
			//builds the post string
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(baseUrl.concat(action));
			//sets the config and content
			httpPost.addHeader("Content-Type" , "application/json; charset=UTF-8");
			httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(request)));
			logger.info(
					"Preparation complete, transferring to {} from {}..." , request.getCreditAcctNo() ,
					request.getDebitAcctNo()
					   );
			//executes the request
			HttpResponse httpResponse = httpClient.execute(httpPost);
			//handle response
			String entityString = EntityUtils.toString(httpResponse.getEntity());
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			//if the response code is anything but OK (200), an exception is thrown
			if ( statusCode != 200 ) throw new HttpException(statusCode + " " + entityString);
			logger.info("Transfer to {} from {} successful." , request.getCreditAcctNo() , request.getDebitAcctNo());
			return objectMapper.readValue(entityString , TransferResponse.class);
		} catch ( IOException e ) {
			throw new HttpException("Failed to map transfer response." , e);
		}
	}

	@Autowired
	public void setObjectMapper ( ObjectMapper objectMapper ) { this.objectMapper = objectMapper; }
}
