package com.fawry;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSContextDemo {

  public static void main(String[] args) throws NamingException {
    InitialContext context = new InitialContext();

    Queue queue = (Queue) context.lookup("queue/myQueue");

    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = factory.createContext()) {

      jmsContext.createProducer().send(queue, "Hello World");

      String message = jmsContext.createConsumer(queue).receiveBody(String.class);

      System.out.println(message);
    }
  }
}
