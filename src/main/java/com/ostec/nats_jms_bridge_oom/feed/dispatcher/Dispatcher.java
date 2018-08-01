/**
 * 
 */
package com.ostec.nats_jms_bridge_oom.feed.dispatcher;

import java.nio.charset.StandardCharsets;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ostec.nats_jms_bridge_oom.feed.jms_sender.ProcessServiceMessageSender;

import io.nats.client.Connection;

/**
 * @author ossiejam
 *
 */
@Component
public class Dispatcher {
	private static final Logger LOGGER = LogManager.getLogger(Dispatcher.class.getName());
	
	@Value("${subscribe.subject}")
	private String subject ;
	@Autowired
	private FeedQueue feedQueue ;
	
	
	public void register(Connection connection) {
		io.nats.client.Dispatcher d = connection.createDispatcher((msg) -> {
		    String response = new String(msg.getData(), StandardCharsets.UTF_8);
		    //LOGGER.info(response);
		    //processServiceMessageSender.send(response);
		    feedQueue.add(response);
		});

		d.subscribe(subject);
		//connection.flush(null);
	}
	
}
