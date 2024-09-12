package com.fawry.security;

import com.fawry.model.Employee;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

public class SecurityApp {

  public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
    InitialContext initialContext = new InitialContext();

    Topic topic = (Topic) initialContext.lookup("topic/empTopic");

    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = factory.createContext()) {

      jmsContext.setClientID("securityApp");

      JMSConsumer consumer = jmsContext.createDurableConsumer(topic,"subscription1");

      consumer.close();

      Thread.sleep(10000);
      consumer = jmsContext.createDurableConsumer(topic,"subscription1");
      Message message = consumer.receive();
      Employee employee = message.getBody(Employee.class);

      System.out.println(
          "Message Received: "
              + employee.getFirstname()
              + " "
              + employee.getLastname()
              + " "
              + employee.getEmail());

      consumer.close();
      jmsContext.unsubscribe("subscription1");
    }


  }
}
