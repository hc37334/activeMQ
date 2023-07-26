package com.maximus;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;


@EnableJms
@SpringBootApplication
public class ActiveMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveMqApplication.class, args);
	}
	public static void main_(String[] args) {
	      
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("http://uvaadmmapp06qic.maxcorp.maximus:61637");
            connectionFactory.setUserName("admin");
            connectionFactory.setPassword("admin");
            
            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST-LOCAL");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            String text = "Hello world! From: using HTTP " ;
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    
}


	
	@Bean
	public  ConnectionFactory  connectionFactory() {
		
		  ActiveMQConnectionFactory connectionFactory = null;
		  
		  try { 
			  
			   System.out.println(" 1 Run Main ********************************* ");
		       
		        connectionFactory = new ActiveMQConnectionFactory("http://uvaadmmapp06qic.maxcorp.maximus:61637");
                connectionFactory.setUserName("admin");
                connectionFactory.setPassword("admin");
		        
		      
		        System.out.println("2 after config factory ********************************* ");       
		        
		      //https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.2/html/configuring_messaging/scheduling_messages
		       // activeMQConnectionFactory.setClientID("dots-queue");
		        
	           JmsTemplate jmsTemplate = new JmsTemplate();
		       
		        jmsTemplate.setConnectionFactory(connectionFactory);
		      
		       
		        jmsTemplate.convertAndSend("dots-queue-jms-local-5", "test message for JMS listener from dots-queue-jms-local-3");
	        
		        System.out.println("3 after config factory ********************************* ");  
		        
		      //  System.out.println(" 3. after sending the message ********************************* ");
		        
			      Object a1 =  jmsTemplate.receiveAndConvert("dots-queue-jms-local-5");
			       
			    System.out.println(" 4. Received message *******************************: " + a1.toString());
		       
		        for(int i=0;  i<10; i++) {
		        	
		        	
		           jmsTemplate.convertAndSend(  "dots-queue", "Delay the message 10000*********** " + i ,m -> {
		        	   try {
		        		   m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, System.currentTimeMillis() + 100000 );
		        	   
						  // m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
						  // m.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
						    
					     
		        	   }catch(Exception ex) {ex.printStackTrace();}
		        	   
		        	   return m;
				    });
			        
		           System.out.println("************************** delay the message");
		        
	
		        }
		       
		        
//		        jmsTemplate.convertAndSend("dots-queue", "test message");
//		        
//		       Object a =  jmsTemplate.receiveAndConvert("dots-queue");
//		       
//		       System.out.println("************************** 1 Received message: " + a.toString());
//		        a =  jmsTemplate.receiveAndConvert("dots-queue");
//		       System.out.println("************************** 2 Received message: " + a.toString());
//		        
		    }catch(Exception ex) {
		    	  System.out.println("********************************************** in main ");
		    	ex.printStackTrace();
		    	
		    
		    }  
		    
		  return connectionFactory;
	}

	
}

/*redelivery-delay=5000, redelivery-delay-multiplier=2, max-redelivery-delay=15000

1. Delivery Attempt 1. (Unsuccessful)
2. Wait Delay Period: 5000
3. Delivery Attempt 2. (Unsuccessful)
4. Wait Delay Period: 10000                   // (5000  * 2) < max-delay-period.  Use 10000
5. Delivery Attempt 3: (Unsuccessful)
6. Wait Delay Period: 15000  
*/
//   m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, System.currentTimeMillis() + 5000000);
//  m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,  1000000);
	       //m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
	 //  m.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
	    
  