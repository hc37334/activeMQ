package com.maximus;


import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.broker.jmx.QueueViewMBean;

import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.management.openmbean.CompositeData;

//@Service
public class DLQHandler {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	public DLQHandler( JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
//	@Autowired
	RemoteJMXBrokerFacade brokerQuery;
	
	public int browse() throws JMSException {

        int count = this.jmsTemplate.browse("ActiveMQ.DLQ", new BrowserCallback<Integer>() {
            
        	@Override
        	public Integer doInJms( Session session,  QueueBrowser browser) throws JMSException {
                Enumeration<?> enumeration = browser.getEnumeration();
                int counter = 0;
                while (enumeration.hasMoreElements()) {
                    Message msg = (Message) enumeration.nextElement();

                  //  System.out.println(String.format("\tFound : %s", msg));
                    counter += 1;
                    System.out.println("********************** Count: " + counter);
                }
                
               String secector = browser.getMessageSelector();
               
             

               System.out.println(String.format("\tsecector : %s", secector));
                return counter;
            }

			
        });

        System.out.println(String.format("\tThere are %s messages", count));
        
        return count;
    }
	
	//http://weblog.plexobject.com/?p=1672
	public void redeliverDLQUsingJmsTemplateReceive(final String from,

			final String to) {
		
		try {
			jmsTemplate.setReceiveTimeout(100);
			Message message = null;

			
			
			while ((message = jmsTemplate.receive(from)) != null) {
				
				final String messageBody = ((TextMessage) message).getText();
				jmsTemplate.send(to, new MessageCreator() {

					@Override
					public Message createMessage(final Session session) throws JMSException {

						return session.createTextMessage(messageBody);
					}
				});
			}
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	// Moving Messages using ActiveMQ’s API
	// Finally, the best approach I found was to use ActiveMQ’s APIs to move
	// messags, e.g.

	public void redeliverDLQUsingJMX(final String brokerName, final String from,

			final String to) {
		try {
		
			
			
			final QueueViewMBean queue = brokerQuery.getQueue(from);

			for (int i = 0; i < 10 && queue.getQueueSize() > 0; i++) {
				CompositeData[] compdatalist = queue.browse();
				
				for (CompositeData cdata : compdatalist) {
					String messageID = (String) cdata.get("JMSMessageID");
					queue.moveMessageTo(messageID, to);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


}
