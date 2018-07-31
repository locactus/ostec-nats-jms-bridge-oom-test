/**
 * 
 */
package com.ostec.nats_jms_bridge.connect;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
/**
 * @author ossiejam
 *
 */
@Service
public class PolygonIOConnectionManager {

	private static final Logger LOGGER = LogManager.getLogger(PolygonIOConnectionManager.class.getName());
	
	
	@Value("${polygon.io.nat.server1}")
	private String natServer1;	
	@Value("${polygon.io.nat.server2}")
	private String natServer2;
	@Value("${polygon.io.nat.server3}")
	private String natServer3;
	@Value("${polygon.io.api.token}")
	private String apiToken;
	
	
	public Connection getConnection() {
		LOGGER.info("Init Polygon IO connection");
		Options o = new Options.Builder().server(natServer1).server(natServer2).server(natServer3).maxReconnects(-1).token(apiToken).build();
		Connection nc = null ;
		try {
			nc = Nats.connect(o);
			LOGGER.info("Connected");
		} catch (IOException | InterruptedException e) {
			LOGGER.error("Init Polygon IO connection failed",e);
		}
		return nc ;
	}
	
}
