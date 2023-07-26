package com.maximus;


import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

//import lombok.extern.log4j.Log4j2;

import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//import org.springframework.jms.core.JmsTemplate;


//mport org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableJms
@SpringBootApplication
@Configuration
@ComponentScan("com.maximus")
public class SpringLdapSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringLdapSecurityApplication.class, args);
		//long i = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes().length;
		
		//System.out.print(i);
	}
	
	
	
	@Bean
	public  ConnectionFactory  connectionFactory() {
		
		  ActiveMQConnectionFactory connectionFactory = null;
		  
		  try { 
			  //http://uvaadmmapp06qic.maxcorp.maximus:12050
			  //https://subscription.packtpub.com/book/application_development/9781782169413/1/ch01lvl1sec20/using-failover-transport-advanced
			  //https://qicmsg-dev-aws.maximus.com:443/qp-data-services/qp-messaging
			  //active-mq.broker-url=http://uvaadmmapp06qic.maxcorp.maximus:61637
			   System.out.println(" 1 Run Main ********************************* failover: ");
			   
		        connectionFactory = new ActiveMQConnectionFactory("failover:(https://qicmsg-dev-aws.maximus.com:443/qp-data-services/qp-messaging)?timeout=1000");
                connectionFactory.setUserName("admin");
                connectionFactory.setPassword("admin");
		        

                RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
                redeliveryPolicy.setInitialRedeliveryDelay(0); // 5minutes
                redeliveryPolicy.setBackOffMultiplier(2);
                redeliveryPolicy.setUseExponentialBackOff( true);
                redeliveryPolicy.setMaximumRedeliveries(2);
               
                redeliveryPolicy.setCollisionAvoidancePercent( (short)1 );
              
                connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		  }catch(Exception ex) {
			  ex.printStackTrace();
		  }
		  return connectionFactory;
	}
	
	//@Bean
//	@Autowired
	public void sendMessage(JmsTemplate jmsTemplate) {
		
		 
		  
		  try { 

		       
		        
		       
		        for(int i=0;  i<10; i++) {
		        	
		         	try {
				           jmsTemplate.convertAndSend(  "dots-queue-jms-delay", "Delay the message 100000 dots-queue-jms-delay *********** " + i ,m -> {
				        	   try {
				        		//   m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,  100 );
				        		 
				        	   }catch(Exception ex) {ex.printStackTrace();}
				        	   
				        	   return m;
						    });
			        
				           	System.out.println("************************** failover the message: " + i);
		         	}catch(Exception ex) {
		         		
		         	}
	
		        }
		             
		    }catch(Exception ex) {
		    	  System.out.println("********************************************** in main ");
		    	ex.printStackTrace();
		    	
		    
		    }  
		    
		 
	}

	@Bean
	public JmsTemplate jmsTemplate(){
		
	     
	      //https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.2/html/configuring_messaging/scheduling_messages
	       // activeMQConnectionFactory.setClientID("dots-queue");
	        
         JmsTemplate jmsTemplate = new JmsTemplate();
	       
	        jmsTemplate.setConnectionFactory(connectionFactory());
	       // jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
	       
	       // jmsTemplate.setDefaultDestinationName("dots-queue-jms-delay");
	        
	        
	        //---------------------------------
	        jmsTemplate.setDeliveryPersistent(true);
	        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
	        jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
	        
	        
	       // jmsTemplate.browse("ActiveMQ.DLQ")
	       
	      //  jmsTemplate.convertAndSend("ActiveMQ.DLQ", "test message for JMS listener from dots-queue-jms-delay");
	        
	       // jmsTemplate.convertAndSend("dots-queue-jms-delay", "test message for JMS listener from dots-queue-jms-delay");
	        return jmsTemplate;
	}

	
	
	

	  @Bean
	  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory () {
		  DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		    factory.setConnectionFactory(connectionFactory());
		  
		    //factory.setMessageConverter(jacksonJmsMessageConverter());
		    //factory.setErrorHandler(doTSErrorHandler());
		   // factory.setExceptionListener(doTSExceptionListener());
		    factory.setConcurrency("1");
		    factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

	      return factory;
	  }
	
	  @Bean
	  public DLQHandler DLQHandler() {
		  DLQHandler a = new DLQHandler(jmsTemplate());
		  try {
			  int count = a.browse();
			  System.out.println("********************** count + " + count);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return a;
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
	    
  