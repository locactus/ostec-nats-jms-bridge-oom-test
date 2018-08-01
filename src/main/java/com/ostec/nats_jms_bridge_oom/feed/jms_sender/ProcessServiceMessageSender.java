package com.ostec.nats_jms_bridge_oom.feed.jms_sender;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class ProcessServiceMessageSender implements CommandLineRunner {

	@Autowired
	private JmsTemplate jmsOutputQueueTemplate;


	@Override
	public void run(String... args) throws Exception {
		
	}
	
	public void send(String tick) {
		
		jmsOutputQueueTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(tick);
				message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
				System.out.println("tick="+tick);
				return message;
			}
		});
		
	}
	
}

