package com.ostec.nats_jms_bridge_oom.feed.dispatcher;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ostec.nats_jms_bridge_oom.feed.jms_sender.ProcessServiceMessageSender;


@Component
public class FeedQueue extends Thread{
	private static final Logger LOGGER = LogManager.getLogger(FeedQueue.class.getName());
	
	private List<String> queue = new ArrayList<String>();
	@Autowired
	private ProcessServiceMessageSender processServiceMessageSender ;
	
	public void add(String item) {
		queue.add(item);
	}

	
	@Override
	public void run() {
		LOGGER.info("processQueue");
		while(true) {
			try {
				if(!queue.isEmpty()) {
					String tick = queue.remove(0);
					processServiceMessageSender.send(tick);
					//LOGGER.info(tick);
				}else {
					Thread.sleep(100);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
