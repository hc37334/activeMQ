package com.maximus;

import javax.jms.Message;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MsgListener {

	
	  @JmsListener(destination = "${qname}")
     public void receiveMessage(Message message, String msg) throws Exception {

         try {
        	 System.out.println("*********************** " + msg);
             // start processing the message. I have string message(msg)
            //Message object hold the header with more information about the message


             throw new Exception("Exception has occoured while trying to process the message : " + msg);
         } catch (Exception e) {

        	 System.out.println("*********************** ");
        	 
             throw e;
         }
     }
}
