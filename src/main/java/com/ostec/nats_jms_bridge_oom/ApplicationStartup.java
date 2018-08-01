package com.ostec.nats_jms_bridge_oom;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ostec.nats_jms_bridge_oom.connect.PolygonIOConnectionManager;
import com.ostec.nats_jms_bridge_oom.feed.dispatcher.Dispatcher;
import com.ostec.nats_jms_bridge_oom.feed.dispatcher.FeedQueue;


@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LogManager.getLogger(ApplicationStartup.class.getName());

	private boolean appStarted = false;
	
	@Autowired
	private PolygonIOConnectionManager polygonIOConnectionManager ;
	@Autowired
	private Dispatcher dispatcher ;
	@Autowired
	private FeedQueue feedQueue ;
	
	
	/**
	 * This event is executed as late as conceivably possible to indicate that the
	 * application is ready to service requests.
	 */
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		appStarted = true;
		feedQueue.setName("Feed Queue");
		feedQueue.start();
		
		dispatcher.register(polygonIOConnectionManager.getConnection());
		
		
		
		return;
	}
	
	
	public boolean appStarted() {
		return this.appStarted ;
	}

}