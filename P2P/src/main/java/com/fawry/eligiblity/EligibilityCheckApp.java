package com.fawry.eligiblity;

import com.fawry.eligiblity.listeners.EligibilityCheckListener;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckApp {

  public static void main(String[] args) throws NamingException, InterruptedException {
    InitialContext initialContext = new InitialContext();
    Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

    try (ActiveMQJMSConnectionFactory cf = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = cf.createContext()) {

      JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
      JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);

      for(int i = 0; i < 10; i+=2) {
        System.out.println("Consumer 1: "+consumer1.receive());
        System.out.println("Consumer 2: "+consumer2.receive());
      }
//      consumer.setMessageListener(new EligibilityCheckListener());

      // Using Lambda Expression   &&  Method Reference
//      consumer.setMessageListener(message -> System.out.println(message));
//      consumer.setMessageListener(System.out::println);

//      Thread.sleep(1000000000);
    }
  }
}
