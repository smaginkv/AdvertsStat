package ru.planetavto.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsAdvertMessagingService {
	private JmsTemplate jms;

	@Autowired
	public JmsAdvertMessagingService(JmsTemplate jms) {
		super();
		this.jms = jms;
	}

	public void sendAdvert(String message) {
	    jms.convertAndSend("NewOrModifiedAdvert", message);
	  }
}
