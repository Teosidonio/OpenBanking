package mz.co.standardbank.e_biller.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
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
 * @author - C816346 on 2020/05/13
 */
@Component
public class SmsGateway {

	private Logger logger = LogManager.getLogger(this);
	private ObjectMapper objectMapper;

	@Value( "${sms.gateway.url}" )
	private String gatewayUrl;

	//TODO clean up exception handling since everything is currently under the IOException umbrella

	public void forwardPayload ( @NotNull Sms sms ) throws HttpException, SmsGatewayException {
		try {
			logger.info("Preparing sms for number {}..." , sms.getRecipient());
			//builds the post string
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(gatewayUrl);
			//sets the config and content
			httpPost.addHeader("Content-Type" , "application/json; charset=UTF-8");
			httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(sms)));
			logger.info("Preparation complete, sending sms to {}..." , sms.getRecipient());
			//executes the request
			HttpResponse httpResponse = httpClient.execute(httpPost);
			//handle response
			String entityString = EntityUtils.toString(httpResponse.getEntity());
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			//if the response code is anything but OK (200), an exception is thrown
			if ( statusCode > 299 ) throw new SmsGatewayException(statusCode + " " + entityString);
			logger.info("SMS submitted for {} successfully." , sms.getRecipient());
		} catch ( IOException e ) {
			throw new HttpException("Couldn't reach SMS gateway." , e);
		}
	}

	@Autowired
	public void setObjectMapper ( ObjectMapper objectMapper ) { this.objectMapper = objectMapper; }

}
